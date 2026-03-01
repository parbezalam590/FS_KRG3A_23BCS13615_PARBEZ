import { memo, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  Button,
  Container,
} from "@mui/material";
import LogoutIcon from "@mui/icons-material/Logout";
import DashboardIcon from "@mui/icons-material/Dashboard";
import HistoryIcon from "@mui/icons-material/History";
import LoginIcon from "@mui/icons-material/Login";

const Header = memo(({ title }) => {
  const { isAuthenticated, setIsAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = useCallback(() => {
    setIsAuthenticated(false);
    navigate("/login");
  }, [setIsAuthenticated, navigate]);

  const handleDashboard = useCallback(() => {
    navigate("/");
  }, [navigate]);

  const handleLogs = useCallback(() => {
    navigate("/logs");
  }, [navigate]);

  const handleLogin = useCallback(() => {
    navigate("/login");
  }, [navigate]);

  return (
    <AppBar position="static" sx={{ backgroundColor: "red" }}>
      <Container maxWidth="lg">
        <Toolbar disableGutters>
          <Typography
            variant="h6"
            sx={{
              flexGrow: 1,
              fontWeight: 600,
              letterSpacing: 1,
            }}
          >
            {title}
          </Typography>
          <Box sx={{ display: "flex", gap: 1 }}>
            {isAuthenticated && (
              <>
                <Button
                  color="inherit"
                  startIcon={<DashboardIcon />}
                  onClick={handleDashboard}
                  sx={{
                    textTransform: "none",
                    "&:hover": {
                      backgroundColor: "rgba(255, 255, 255, 0.15)",
                    },
                  }}
                >
                  Dashboard
                </Button>
                <Button
                  color="inherit"
                  startIcon={<DashboardIcon />}
                  onClick={() => navigate('/dashboard/water')}
                  sx={{
                    textTransform: "none",
                    "&:hover": {
                      backgroundColor: "rgba(255, 255, 255, 0.15)",
                    },
                  }}
                >
                  Water Tracker
                </Button>
                <Button
                  color="inherit"
                  startIcon={<HistoryIcon />}
                  onClick={handleLogs}
                  sx={{
                    textTransform: "none",
                    "&:hover": {
                      backgroundColor: "rgba(255, 255, 255, 0.15)",
                    },
                  }}
                >
                  Logs
                </Button>
                <Button
                  color="inherit"
                  startIcon={<LogoutIcon />}
                  onClick={handleLogout}
                  sx={{
                    textTransform: "none",
                    "&:hover": {
                      backgroundColor: "rgba(255, 255, 255, 0.15)",
                    },
                  }}
                >
                  Logout
                </Button>
              </>
            )}
            {!isAuthenticated && (
              <Button
                color="inherit"
                startIcon={<LoginIcon />}
                onClick={handleLogin}
                sx={{
                  textTransform: "none",
                  "&:hover": {
                    backgroundColor: "rgba(255, 255, 255, 0.15)",
                  },
                }}
              >
                Login
              </Button>
            )}
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
});

Header.displayName = "Header";

export default Header;
