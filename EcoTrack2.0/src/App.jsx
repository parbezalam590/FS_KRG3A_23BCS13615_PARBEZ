
import { lazy, Suspense } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Header from "./components/Header.jsx";
import ProtectedRoutes from "./routes/ProtectedRoutes.jsx";
import { Box, CircularProgress } from "@mui/material";

// Lazy load page components for code splitting
const Login = lazy(() => import("./pages/Login.jsx"));
const DashboardLayout = lazy(() => import("./pages/DashboardLayout.jsx"));
const DashboardSummary = lazy(() =>
  import("./pages/DashboardSummary.jsx")
);
const DashboardAnalytics = lazy(() =>
  import("./pages/DashboardAnalytics.jsx")
);
const Logs = lazy(() => import("./pages/Logs.jsx"));
const WaterTracker = lazy(() => import("./pages/WaterTracker.jsx"));

// Loading component for Suspense fallback
const LoadingFallback = () => (
  <Box
    sx={{
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      minHeight: "60vh",
    }}
  >
    <CircularProgress />
  </Box>
);

function App() {
  return (
    <BrowserRouter>
      <Header title="EcoTrack" />
      <Suspense fallback={<LoadingFallback />}>
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" />} />
          <Route path="/login" element={<Login />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoutes>
                <DashboardLayout />
              </ProtectedRoutes>
            }
          >
            <Route index element={<DashboardSummary />} />
            <Route path="summary" element={<DashboardSummary />} />
            <Route path="analytics" element={<DashboardAnalytics />} />
            <Route path="water" element={<WaterTracker />} />
          </Route>
          <Route
            path="/logs"
            element={
              <ProtectedRoutes>
                <Logs />
              </ProtectedRoutes>
            }
          />
        </Routes>
      </Suspense>
    </BrowserRouter>
  );
}

export default App;