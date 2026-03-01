import { logs } from '../../data/logs';

describe('logs data', () => {
  test('contains expected entries and lengths', () => {
    expect(Array.isArray(logs)).toBe(true);
    expect(logs).toHaveLength(8);
  });

  test('first log has required properties', () => {
    const first = logs[0];
    expect(first).toMatchObject({ id: 1, activity: 'Car Travel' });
    expect(typeof first.carbon).toBe('number');
  });

  test('carbon values are non-negative', () => {
    logs.forEach((l) => expect(l.carbon).toBeGreaterThanOrEqual(0));
  });
});
