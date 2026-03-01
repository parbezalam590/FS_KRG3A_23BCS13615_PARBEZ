import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "../context/AuthContext";
import DashboardLayout from "../pages/DashboardLayout";

// a simple dummy component for nested routes
const Dummy = ({ text }) => <div>{text}</div>;

describe("DashboardLayout routing and tabs", () => {
  test("water tab is selected when navigating to /dashboard/water", () => {
    render(
      <MemoryRouter initialEntries={["/dashboard/water"]}>
        <AuthProvider>
          <Routes>
            <Route path="/dashboard" element={<DashboardLayout />}>
              <Route path="water" element={<Dummy text="water page" />} />
            </Route>
          </Routes>
        </AuthProvider>
      </MemoryRouter>
    );

    // confirm dummy child rendered
    expect(screen.getByText(/water page/i)).toBeInTheDocument();
    const tab = screen.getByRole("tab", { name: /water tracker/i });
    expect(tab).toHaveAttribute("aria-selected", "true");
  });
});