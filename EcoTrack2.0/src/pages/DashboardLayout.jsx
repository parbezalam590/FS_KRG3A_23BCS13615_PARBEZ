import { memo, useCallback, useState, useEffect } from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import {
  Container,
  Box,
  Tabs,
  Tab,
  Card,
  CardContent,
  Typography,
} from "@mui/material";
import DashboardIcon from "@mui/icons-material/Dashboard";
import AnalyticsIcon from "@mui/icons-material/Analytics";

const DashboardLayout = memo(() => {
  const [activeTab, setActiveTab] = useState(0);
  const location = useLocation();

  const handleTabChange = useCallback((event, newValue) => {
    setActiveTab(newValue);
  }, []);

  // keep tab in sync with the current path
  useEffect(() => {
    if (location.pathname.endsWith("/water")) {
      setActiveTab(2);
    } else if (location.pathname.endsWith("/analytics")) {
      setActiveTab(1);
    } else {
      setActiveTab(0);
    }
  }, [location]);

  return (
    <Container maxWidth="lg" sx={{ paddingY: 4 }}>
      <Box sx={{ marginBottom: 4 }}>
        <Typography
          variant="h4"
          component="h1"
          sx={{
            fontWeight: 700,
            color: "red",
            marginBottom: 3,
          }}
        >
          Dashboard
        </Typography>

        <Card sx={{ boxShadow: 2 }}>
          <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
            <Tabs
              value={activeTab}
              onChange={handleTabChange}
              aria-label="dashboard navigation"
            >
              <Tab
                icon={<DashboardIcon />}
                iconPosition="start"
                label="Summary"
                component={Link}
                to="summary"
                sx={{
                  textTransform: "none",
                  fontSize: 16,
                }}
              />
              <Tab
                icon={<AnalyticsIcon />}
                iconPosition="start"
                label="Analytics"
                component={Link}
                to="analytics"
                sx={{
                  textTransform: "none",
                  fontSize: 16,
                }}
              />
              <Tab
                icon={<DashboardIcon />}
                iconPosition="start"
                label="Water Tracker"
                component={Link}
                to="water"
                sx={{
                  textTransform: "none",
                  fontSize: 16,
                }}
              />
            </Tabs>
          </Box>

          <CardContent sx={{ paddingY: 4 }}>
            <Outlet />
          </CardContent>
        </Card>
      </Box>
    </Container>
  );
});

DashboardLayout.displayName = "DashboardLayout";

export default DashboardLayout;
