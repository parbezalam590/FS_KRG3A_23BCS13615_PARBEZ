import React, { memo } from "react";
import { Typography } from "@mui/material";

let renderCount = 0;

export const getRenderCount = () => renderCount;
export const resetRenderCount = () => {
  renderCount = 0;
};

const CounterDisplayComponent = ({ count, goal }) => {
  renderCount += 1;
  return (
    <Typography data-testid="counter-display">
      {`${count} / ${goal} glasses completed`}
    </Typography>
  );
};

const CounterDisplay = memo(CounterDisplayComponent);

export default CounterDisplay;