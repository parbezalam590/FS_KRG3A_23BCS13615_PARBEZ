import { memo, useMemo } from "react";
import { useSelector } from "react-redux";
import {
  Container,
  Card,
  CardContent,
  Typography,
  Grid,
  Box,
  LinearProgress,
  Paper,
} from "@mui/material";
import PublicIcon from "@mui/icons-material/Public";
import TrendingUpIcon from "@mui/icons-material/TrendingUp";
import TrendingDownIcon from "@mui/icons-material/TrendingDown";
import BarChartIcon from "@mui/icons-material/BarChart";

const DashboardSummary = memo(() => {
  const { data } = useSelector((state) => state.logs);

  // Memoized statistics calculations
  const stats = useMemo(() => {
    if (data.length === 0) {
      return {
        totalActivities: 0,
        totalCarbon: 0,
        averageCarbon: 0,
        highImpactCount: 0,
        lowImpactCount: 0,
      };
    }

    const totalCarbon = data.reduce((sum, log) => sum + log.carbon, 0);
    const highImpactCount = data.filter((log) => log.carbon > 4).length;
    const lowImpactCount = data.filter((log) => log.carbon <= 4).length;

    return {
      totalActivities: data.length,
      totalCarbon: totalCarbon.toFixed(2),
      averageCarbon: (totalCarbon / data.length).toFixed(2),
      highImpactCount,
      lowImpactCount,
    };
  }, [data]);

  const StatCard = memo(({ icon: Icon, title, value, subtitle, color }) => (
    <Grid item xs={12} sm={6} md={4}>
      <Paper
        elevation={2}
        sx={{
          padding: 3,
          textAlign: "center",
          borderTop: `4px solid ${color}`,
          height: "100%",
        }}
      >
        <Box sx={{ display: "flex", justifyContent: "center", marginBottom: 2 }}>
          <Icon sx={{ fontSize: 40, color }} />
        </Box>
        <Typography
          variant="body2"
          sx={{
            color: "textSecondary",
            marginBottom: 1,
            fontWeight: 500,
          }}
        >
          {title}
        </Typography>
        <Typography
          variant="h5"
          sx={{
            fontWeight: 700,
            color: color,
            marginBottom: 1,
          }}
        >
          {value}
        </Typography>
        {subtitle && (
          <Typography variant="caption" sx={{ color: "textSecondary" }}>
            {subtitle}
          </Typography>
        )}
      </Paper>
    </Grid>
  ));

  StatCard.displayName = "StatCard";

  return (
    <Container maxWidth="lg" sx={{ paddingY: 4 }}>
      <Box sx={{ marginBottom: 4 }}>
        <Typography
          variant="h5"
          sx={{
            fontWeight: 700,
            color: "red",
            marginBottom: 3,
          }}
        >
          Dashboard Summary
        </Typography>
        <Typography
          variant="body1"
          sx={{
            color: "textSecondary",
            marginBottom: 3,
          }}
        >
          Welcome to the dashboard. 
        </Typography>
      </Box>

      <Grid container spacing={3} sx={{ marginBottom: 4 }}>
        <StatCard
          icon={BarChartIcon}
          title="Total Activities"
          value={stats.totalActivities}
          color="#0000FF"
        />
        <StatCard
          icon={PublicIcon}
          title="Total Carbon Emission"
          value={`${stats.totalCarbon} kg`}
          subtitle="CO₂ Equivalent"
          color="red"
        />
        <StatCard
          icon={BarChartIcon}
          title="Average Carbon per Activity"
          value={`${stats.averageCarbon} kg`}
          color="#FFD700"
        />
      </Grid>

      <Grid container spacing={3}>
        <Grid item xs={12} sm={6}>
          <Card sx={{ boxShadow: 2 }}>
            <CardContent>
              <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                <TrendingUpIcon sx={{ color: "#d32f2f", fontSize: 24 }} />
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: 700,
                    color: "#d32f2f",
                  }}
                >
                  High Impact Activities
                </Typography>
              </Box>
              <Typography
                variant="h4"
                sx={{
                  fontWeight: 700,
                  marginTop: 2,
                  marginBottom: 1,
                  color: "#d32f2f",
                }}
              >
                {stats.highImpactCount}
              </Typography>
              <LinearProgress
                variant="determinate"
                value={
                  stats.totalActivities > 0
                    ? (stats.highImpactCount / stats.totalActivities) * 100
                    : 0
                }
                sx={{
                  height: 8,
                  borderRadius: 4,
                  backgroundColor: "#f0f0f0",
                  "& .MuiLinearProgress-bar": {
                    backgroundColor: "#d32f2f",
                  },
                }}
              />
              <Typography
                variant="caption"
                sx={{ color: "textSecondary", marginTop: 1, display: "block" }}
              >
                {stats.totalActivities > 0
                  ? Math.round(
                      (stats.highImpactCount / stats.totalActivities) * 100
                    )
                  : 0}
                % of activities
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6}>
          <Card sx={{ boxShadow: 2 }}>
            <CardContent>
              <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                <TrendingDownIcon sx={{ color: "#2ecc71", fontSize: 24 }} />
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: 700,
                    color: "#2ecc71",
                  }}
                >
                  Low Impact Activities
                </Typography>
              </Box>
              <Typography
                variant="h4"
                sx={{
                  fontWeight: 700,
                  marginTop: 2,
                  marginBottom: 1,
                  color: "#2ecc71",
                }}
              >
                {stats.lowImpactCount}
              </Typography>
              <LinearProgress
                variant="determinate"
                value={
                  stats.totalActivities > 0
                    ? (stats.lowImpactCount / stats.totalActivities) * 100
                    : 0
                }
                sx={{
                  height: 8,
                  borderRadius: 4,
                  backgroundColor: "#f0f0f0",
                  "& .MuiLinearProgress-bar": {
                    backgroundColor: "#2ecc71",
                  },
                }}
              />
              <Typography
                variant="caption"
                sx={{ color: "textSecondary", marginTop: 1, display: "block" }}
              >
                {stats.totalActivities > 0
                  ? Math.round(
                      (stats.lowImpactCount / stats.totalActivities) * 100
                    )
                  : 0}
                % of activities
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Container>
  );
});

DashboardSummary.displayName = "DashboardSummary";

export default DashboardSummary;