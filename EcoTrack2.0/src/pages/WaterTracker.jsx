import React, { useState, useEffect, useCallback, useMemo } from "react";
import {
  Container,
  Box,
  Button,
  Typography,
  TextField,
  Card,
  CardContent,
  CircularProgress,
} from "@mui/material";
import CounterDisplay from "../components/CounterDisplay";

const WaterTracker = () => {
  const [count, setCount] = useState(0);
  const [goal, setGoal] = useState(8);
  const [inputGoal, setInputGoal] = useState(8);
  const [tip, setTip] = useState("");
  const [loadingTip, setLoadingTip] = useState(false);
  const [errorTip, setErrorTip] = useState(null);
  const [dummy, setDummy] = useState(0);

  // load from localStorage on mount
  useEffect(() => {
    const saved = localStorage.getItem("waterCount");
    if (saved !== null) {
      const num = parseInt(saved, 10);
      if (!isNaN(num)) setCount(num);
    }
    const savedGoal = localStorage.getItem("waterGoal");
    if (savedGoal !== null) {
      const g = parseInt(savedGoal, 10);
      if (!isNaN(g)) {
        setGoal(g);
        setInputGoal(g);
      }
    }
  }, []);

  // persist count and goal
  useEffect(() => {
    localStorage.setItem("waterCount", count);
  }, [count]);

  useEffect(() => {
    localStorage.setItem("waterGoal", goal);
  }, [goal]);

  // fetch advice
  useEffect(() => {
    let cancelled = false;
    const fetchTip = async () => {
      setLoadingTip(true);
      setErrorTip(null);
      try {
        const res = await fetch("https://api.adviceslip.com/advice");
        if (!res.ok) throw new Error("Network response was not ok");
        const data = await res.json();
        if (!cancelled) setTip(data.slip.advice);
      } catch (err) {
        if (!cancelled) setErrorTip(err.message);
      } finally {
        if (!cancelled) setLoadingTip(false);
      }
    };
    fetchTip();
    return () => {
      cancelled = true;
    };
  }, []);

  const handleAdd = useCallback(() => setCount((c) => c + 1), []);
  const handleRemove = useCallback(
    () => setCount((c) => Math.max(0, c - 1)),
    []
  );
  const handleReset = useCallback(() => setCount(0), []);
  const handleGoalChange = useCallback((e) => setInputGoal(e.target.value), []);
  const saveGoal = useCallback(() => {
    const num = Number(inputGoal);
    if (!isNaN(num) && num >= 0) {
      setGoal(num);
    }
  }, [inputGoal]);

  // memoize props object to avoid triggering CounterDisplay when unrelated state changes
  const displayProps = useMemo(() => ({ count, goal }), [count, goal]);
  const goalReached = count >= goal;
  const handleDummy = useCallback(() => setDummy((d) => d + 1), []);

  return (
    <Container maxWidth="sm" sx={{ paddingY: 4 }}>
      <Typography variant="h4" sx={{ color: "red", fontWeight: 700, mb: 2 }}>
        Water Intake Tracker
      </Typography>
      <Card sx={{ boxShadow: 2, mb: 4 }}>
        <CardContent>
          <Box sx={{ display: "flex", justifyContent: "center", gap: 2, mb: 2 }}>
            <Button variant="contained" onClick={handleAdd}>
              +
            </Button>
            <Button variant="contained" onClick={handleRemove}>
              -
            </Button>
            <Button variant="outlined" onClick={handleReset}>
              Reset
            </Button>
          </Box>
          <CounterDisplay {...displayProps} />
          {goalReached && (
            <Typography sx={{ color: "green", mt: 1 }}>Goal Reached 🎉</Typography>
          )}
          <Box sx={{ mt: 3 }}>
            <TextField
              label="Daily goal"
              type="number"
              value={inputGoal}
              onChange={handleGoalChange}
              size="small"
            />
            <Button sx={{ ml: 2 }} onClick={saveGoal}>
              Set Goal
            </Button>
          </Box>
        </CardContent>
      </Card>

      <Card sx={{ boxShadow: 2, mb: 4 }}>
        <CardContent>
          {loadingTip && <Typography>Loading tip...</Typography>}
          {errorTip && <Typography color="error">Error: {errorTip}</Typography>}
          {!loadingTip && !errorTip && tip && (
            <Typography>Today’s Health Tip: {tip}</Typography>
          )}
        </CardContent>
      </Card>

      <Button onClick={handleDummy} data-testid="dummy-button">
        Unrelated Action ({dummy})
      </Button>
    </Container>
  );
};

export default WaterTracker;