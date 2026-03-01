import React from 'react';
import { render, screen } from '@testing-library/react';
import Header from '../../components/Header';
import { AuthProvider } from '../../context/AuthContext';
import { MemoryRouter } from 'react-router-dom';

jest.mock('react-router-dom', () => ({
  ...(jest.requireActual('react-router-dom')),
  useNavigate: () => jest.fn(),
}));

describe('Header component', () => {
  test('renders title and Login when unauthenticated', () => {
    render(
      <MemoryRouter>
        <AuthProvider>
          <Header title="EcoTrack" />
        </AuthProvider>
      </MemoryRouter>
    );

    expect(screen.getByText('EcoTrack')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /login/i })).toBeInTheDocument();
  });

  test('renders dashboard, water tracker, logs, and logout when authenticated', () => {
    // write token before mounting so AuthProvider initializes as authenticated
    localStorage.setItem('token', 'fake');
    render(
      <MemoryRouter>
        <AuthProvider>
          <Header title="EcoTrack" />
        </AuthProvider>
      </MemoryRouter>
    );

    expect(screen.getByRole('button', { name: /dashboard/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /water tracker/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /logs/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /logout/i })).toBeInTheDocument();
  });

  test('matches snapshot (unauthenticated)', () => {
    const { container } = render(
      <MemoryRouter>
        <AuthProvider>
          <Header title="EcoTrack" />
        </AuthProvider>
      </MemoryRouter>
    );
    expect(container).toMatchSnapshot();
  });

  test('matches snapshot (authenticated)', () => {
    localStorage.setItem('token', 'fake');
    const { container } = render(
      <MemoryRouter>
        <AuthProvider>
          <Header title="EcoTrack" />
        </AuthProvider>
      </MemoryRouter>
    );
    expect(container).toMatchSnapshot();
  });
});
