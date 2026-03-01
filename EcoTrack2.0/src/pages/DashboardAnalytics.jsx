import { memo, useMemo } from "react";
import { useSelector } from "react-redux";
import {
  Container,
  Card,
  CardContent,
  Typography,
  Grid,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
} from "@mui/material";
import AssessmentIcon from "@mui/icons-material/Assessment";
import TrendingUpIcon from "@mui/icons-material/TrendingUp";

const DashboardAnalytics = memo(() => {
  const { data } = useSelector((state) => state.logs);

  // Memoized analytics data
  const analyticsData = useMemo(() => {
    if (data.length === 0) return { sorted: [], maxCarbon: 0, minCarbon: 0 };

    const sorted = [...data].sort((a, b) => b.carbon - a.carbon);
    const maxCarbon = Math.max(...data.map((log) => log.carbon), 0);
    const minCarbon = Math.min(...data.map((log) => log.carbon), 0);

    return { sorted, maxCarbon, minCarbon };
  }, [data]);

  const getImpactLevel = (carbon) => {
    if (carbon > 10) return { label: "Critical", color: "error" };
    if (carbon > 6) return { label: "High", color: "warning" };
    if (carbon > 3) return { label: "Medium", color: "info" };
    return { label: "Low", color: "success" };
  };

  return (
    <Container maxWidth="lg" sx={{ paddingY: 4 }}>
      <Box sx={{ marginBottom: 4 }}>
        <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
          <AssessmentIcon sx={{ color: "red", fontSize: 32 }} />
          <Typography
            variant="h5"
            sx={{
              fontWeight: 700,
              color: "red",
            }}
          >
          Dashboard Analytics
          </Typography>
        </Box>
        <Typography
          variant="body1"
          sx={{
            color: "textSecondary",
            marginTop: 1,
          }}
        >
          Detailed analysis
        </Typography>
      </Box>

      {data.length > 0 ? (
        <Grid container spacing={3}>
          {/* Activity Impact Ranking */}
          <Grid item xs={12}>
            <Card sx={{ boxShadow: 2 }}>
              <CardContent>
                <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                  <TrendingUpIcon sx={{ color: "#1976d2", fontSize: 24 }} />
                  <Typography
                    variant="h6"
                    sx={{
                      fontWeight: 700,
                      color: "black",
                    }}
                  >
                    Activities Ranked by Carbon Impact
                  </Typography>
                </Box>
                <TableContainer sx={{ marginTop: 2 }}>
                  <Table>
                    <TableHead>
                      <TableRow
                        sx={{
                          backgroundColor: "#f5f5f5",
                        }}
                      >
                        <TableCell sx={{ fontWeight: 700 }}>Rank</TableCell>
                        <TableCell sx={{ fontWeight: 700 }}>Activity</TableCell>
                        <TableCell sx={{ fontWeight: 700 }} align="right">
                          Carbon Footprint (kg CO₂)
                        </TableCell>
                        <TableCell sx={{ fontWeight: 700 }} align="center">
                          Impact Level
                        </TableCell>
                        <TableCell sx={{ fontWeight: 700 }} align="right">
                          % of Total
                        </TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {analyticsData.sorted.map((log, index) => {
                        const total = analyticsData.sorted.reduce(
                          (sum, item) => sum + item.carbon,
                          0
                        );
                        const percentage = ((log.carbon / total) * 100).toFixed(
                          1
                        );
                        const impact = getImpactLevel(log.carbon);

                        return (
                          <TableRow
                            key={log.id}
                            sx={{
                              "&:nth-of-type(odd)": {
                                backgroundColor: "#fafafa",
                              },
                              "&:hover": {
                                backgroundColor: "#f0f0f0",
                              },
                            }}
                          >
                            <TableCell sx={{ fontWeight: 600 }}>
                              #{index + 1}
                            </TableCell>
                            <TableCell>{log.activity}</TableCell>
                            <TableCell align="right" sx={{ fontWeight: 600 }}>
                              {log.carbon}
                            </TableCell>
                            <TableCell align="center">
                              <Chip
                                label={impact.label}
                                color={impact.color}
                                variant="outlined"
                                size="small"
                              />
                            </TableCell>
                            <TableCell align="right">
                              <Typography variant="body2" sx={{ fontWeight: 600 }}>
                                {percentage}%
                              </Typography>
                            </TableCell>
                          </TableRow>
                        );
                      })}
                    </TableBody>
                  </Table>
                </TableContainer>
              </CardContent>
            </Card>
          </Grid>

          {/* Statistics Cards */}
          <Grid item xs={12} sm={6} md={3}>
            <Paper
              elevation={2}
              sx={{
                padding: 3,
                textAlign: "center",
                borderTop: "4px solid red",
              }}
            >
              <Typography variant="body2" sx={{ color: "textSecondary" }}>
                Highest Impact Activity
              </Typography>
              <Typography
                variant="h6"
                sx={{
                  fontWeight: 700,
                  marginTop: 1,
                  color: "#d32f2f",
                }}
              >
                {analyticsData.maxCarbon} kg
              </Typography>
            </Paper>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <Paper
              elevation={2}
              sx={{
                padding: 3,
                textAlign: "center",
                borderTop: "4px solid #1976d2",
              }}
            >
              <Typography variant="body2" sx={{ color: "textSecondary" }}>
                Lowest Impact Activity
              </Typography>
              <Typography
                variant="h6"
                sx={{
                  fontWeight: 700,
                  marginTop: 1,
                  color: "blue",
                }}
              >
                {analyticsData.minCarbon} kg
              </Typography>
            </Paper>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <Paper
              elevation={2}
              sx={{
                padding: 3,
                textAlign: "center",
                borderTop: "4px solid green",
              }}
            >
              <Typography variant="body2" sx={{ color: "textSecondary" }}>
                Total Activities
              </Typography>
              <Typography
                variant="h6"
                sx={{
                  fontWeight: 700,
                  marginTop: 1,
                  color: "green",
                }}
              >
                {data.length}
              </Typography>
            </Paper>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <Paper
              elevation={2}
              sx={{
                padding: 3,
                textAlign: "center",
                borderTop: "4px solid #ff9800",
              }}
            >
              <Typography variant="body2" sx={{ color: "textSecondary" }}>
                Total Carbon
              </Typography>
              <Typography
                variant="h6"
                sx={{
                  fontWeight: 700,
                  marginTop: 1,
                  color: "#ff9800",
                }}
              >
                {(analyticsData.sorted.reduce((sum, item) => sum + item.carbon, 0)).toFixed(2)} kg
              </Typography>
            </Paper>
          </Grid>
        </Grid>
      ) : (
        <Card sx={{ boxShadow: 2 }}>
          <CardContent sx={{ textAlign: "center", paddingY: 4 }}>
            <Typography variant="body1" sx={{ color: "textSecondary" }}>
              No activity data available yet. Load data to see analytics.
            </Typography>
          </CardContent>
        </Card>
      )}
    </Container>
  );
});

DashboardAnalytics.displayName = "DashboardAnalytics";

export default DashboardAnalytics;