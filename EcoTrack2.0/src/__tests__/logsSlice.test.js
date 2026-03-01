import logsReducer, { fetchLogs } from '../store/logsSlice';

describe('logsSlice reducer', () => {
  const initialState = { data: [], status: 'idle', error: null };

  test('should return the initial state', () => {
    expect(logsReducer(undefined, { type: '@@INIT' })).toEqual(initialState);
  });

  test('handles fetchLogs.pending', () => {
    const action = { type: fetchLogs.pending.type };
    const state = logsReducer(initialState, action);
    expect(state.status).toBe('loading');
  });

  test('handles fetchLogs.fulfilled', () => {
    const payload = [{ id: 1, activity: 'Test', carbon: 1 }];
    const action = { type: fetchLogs.fulfilled.type, payload };
    const state = logsReducer(initialState, action);
    expect(state.status).toBe('succeeded');
    expect(state.data).toEqual(payload);
  });

  test('handles fetchLogs.rejected', () => {
    const action = { type: fetchLogs.rejected.type, error: { message: 'fail' } };
    const state = logsReducer(initialState, action);
    expect(state.status).toBe('failed');
    expect(state.error).toBe('fail');
  });
});
