import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import WaterTracker from "../pages/WaterTracker";
import { getRenderCount, resetRenderCount } from "../components/CounterDisplay";

// ensure localStorage is clean between tests
beforeEach(() => {
  localStorage.clear();
  resetRenderCount();
});

describe("WaterTracker page", () => {
  test("initializes with default values and loads from localStorage", () => {
    localStorage.setItem("waterCount", "3");
    localStorage.setItem("waterGoal", "5");
    render(<WaterTracker />);
    expect(screen.getByTestId("counter-display")).toHaveTextContent("3 / 5 glasses completed");
  });

  test("increment, decrement and reset buttons work and goal message shows", () => {
    render(<WaterTracker />);
    const add = screen.getByText("+");
    const remove = screen.getByText("-");
    const reset = screen.getByText(/reset/i);

    fireEvent.click(add);
    expect(screen.getByTestId("counter-display")).toHaveTextContent("1 / 8 glasses completed");

    fireEvent.click(remove);
    expect(screen.getByTestId("counter-display")).toHaveTextContent("0 / 8 glasses completed");

    fireEvent.click(remove); // shouldn't go negative
    expect(screen.getByTestId("counter-display")).toHaveTextContent("0 / 8 glasses completed");

    // bump to goal to test message
    for (let i = 0; i < 8; i++) {
      fireEvent.click(add);
    }
    expect(screen.getByText(/goal reached/i)).toBeInTheDocument();

    fireEvent.click(reset);
    expect(screen.queryByText(/goal reached/i)).not.toBeInTheDocument();
  });

  test("saving and updating goal persists and updates display", () => {
    render(<WaterTracker />);
    const input = screen.getByLabelText(/daily goal/i);
    const saveBtn = screen.getByText(/save goal/i);

    fireEvent.change(input, { target: { value: "3" } });
    fireEvent.click(saveBtn);

    expect(screen.getByTestId("counter-display")).toHaveTextContent("0 / 3 glasses completed");
    expect(localStorage.getItem("waterGoal")).toBe("3");
  });

  test("health tip loads successfully", async () => {
    const mockAdvice = { slip: { advice: "Stay hydrated" } };
    global.fetch = jest.fn(() =>
      Promise.resolve({ ok: true, json: () => Promise.resolve(mockAdvice) })
    );
    render(<WaterTracker />);
    expect(screen.getByText(/loading tip/i)).toBeInTheDocument();
    await waitFor(() => screen.getByText(/today’s health tip/i));
    expect(screen.getByText(/Stay hydrated/i)).toBeInTheDocument();
  });

  test("health tip error state is shown on fetch failure", async () => {
    global.fetch = jest.fn(() => Promise.reject(new Error("fail")));
    render(<WaterTracker />);
    await waitFor(() => screen.getByText(/error:/i));
    expect(screen.getByText(/fail/i)).toBeInTheDocument();
  });

  test("CounterDisplay does not re-render when unrelated state changes", () => {
    render(<WaterTracker />);
    const initialCount = getRenderCount();
    const dummyButton = screen.getByTestId("dummy-button");
    fireEvent.click(dummyButton);
    expect(getRenderCount()).toBe(initialCount);
  });
});
