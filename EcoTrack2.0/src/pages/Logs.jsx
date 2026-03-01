import { useMemo, memo, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchLogs } from "../store/logsSlice";
import { useEffect } from "react";
import {
  Container,
  Card,
  CardContent,
  Typography,
  List,
  ListItem,
  ListItemText,
  Chip,
  Box,
  CircularProgress,
  Alert,
  Grid,
} from "@mui/material";
import TrendingDownIcon from "@mui/icons-material/TrendingDown";
import TrendingUpIcon from "@mui/icons-material/TrendingUp";

const Logs = memo(() => {
  const dispatch = useDispatch();
  const { data, status, error } = useSelector((state) => state.logs);

  useEffect(() => {
    if (status === "idle") {
      dispatch(fetchLogs());
    }
  }, [status, dispatch]);

  // Memoized filtered data - only recomputes when data changes
  const { highImpactLogs, lowImpactLogs } = useMemo(() => {
    return {
      highImpactLogs: data.filter((log) => log.carbon > 4),
      lowImpactLogs: data.filter((log) => log.carbon <= 4),
    };
  }, [data]);

  const renderLogList = useCallback(
    (logs, isHighImpact) => (
      <List sx={{ width: "100%" }}>
        {logs.map((log) => (
          <ListItem key={log.id} sx={{ paddingY: 1.5 }}>
            <ListItemText
              primary={log.activity}
              secondary={`Carbon Footprint: ${log.carbon} kg CO₂`}
              primaryTypographyProps={{
                fontWeight: 600,
                color: isHighImpact ? "#d32f2f" : "#2ecc71",
              }}
              secondaryTypographyProps={{
                color: "textSecondary",
              }}
            />
            <Chip
              icon={isHighImpact ? <TrendingUpIcon /> : <TrendingDownIcon />}
              label={`${log.carbon} kg`}
              color={isHighImpact ? "error" : "success"}
              variant="outlined"
              sx={{ marginLeft: 2 }}
            />
          </ListItem>
        ))}
      </List>
    ),
    []
  );

  if (status === "loading") {
    return (
      <Container maxWidth="lg" sx={{ paddingY: 4 }}>
        <Box sx={{ display: "flex", justifyContent: "center", paddingY: 4 }}>
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (status === "failed") {
    return (
      <Container maxWidth="lg" sx={{ paddingY: 4 }}>
        <Alert severity="error">
          Error loading logs: {error || "Unknown error"}
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ paddingY: 4 }}>
      <Box sx={{ marginBottom: 4 }}>
        <Typography
          variant="h4"
          component="h1"
          sx={{
            marginBottom: 3,
            fontWeight: 700,
            color: "white",
          }}
        >
          Carbon Footprint Activities
        </Typography>
      </Box>

      <Grid container spacing={3}>
        {/* High Impact Activities */}
        <Grid item xs={12} md={6}>
          <Card sx={{ height: "100%", boxShadow: 2 }}>
            <CardContent>
              <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                <TrendingUpIcon sx={{ color: "#d32f2f", fontSize: 28 }} />
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: 700,
                    color: "#d32f2f",
                  }}
                >
                  High Impact Activities ({highImpactLogs.length})
                </Typography>
              </Box>
              <Typography
                variant="body2"
                sx={{
                  color: "textSecondary",
                  marginBottom: 2,
                  marginTop: 1,
                }}
              >
                Activities with carbon footprint &gt; 4 kg CO₂
              </Typography>
              {highImpactLogs.length > 0 ? (
                renderLogList(highImpactLogs, true)
              ) : (
                <Typography variant="body2" sx={{ color: "textSecondary" }}>
                  No high impact activities found.
                </Typography>
              )}
            </CardContent>
          </Card>
        </Grid>

        {/* Low Impact Activities */}
        <Grid item xs={12} md={6}>
          <Card sx={{ height: "100%", boxShadow: 2 }}>
            <CardContent>
              <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                <TrendingDownIcon sx={{ color: "#2ecc71", fontSize: 28 }} />
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: 700,
                    color: "#2ecc71",
                  }}
                >
                  Low Impact Activities ({lowImpactLogs.length})
                </Typography>
              </Box>
              <Typography
                variant="body2"
                sx={{
                  color: "textSecondary",
                  marginBottom: 2,
                  marginTop: 1,
                }}
              >
                Activities with carbon footprint ≤ 4 kg CO₂
              </Typography>
              {lowImpactLogs.length > 0 ? (
                renderLogList(lowImpactLogs, false)
              ) : (
                <Typography variant="body2" sx={{ color: "textSecondary" }}>
                  No low impact activities found.
                </Typography>
              )}
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Container>
  );
});

Logs.displayName = "Logs";

export default Logs;
