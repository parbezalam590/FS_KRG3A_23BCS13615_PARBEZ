import {logs} from "../data/logs";

const Logs = () => {
    const highImpactLogs = logs.filter((log) => log.carbon > 4);
    const lowImpactLogs = logs.filter((log) => log.carbon <= 4);
    return (
        <div>
            <h2>High Carbon Activities ({'>'} 4Kg)</h2>
            <ul>
                {highImpactLogs.map((log) => (
                    <li style={{color:"rgb(224, 31, 31)"}} key={log.id}>
                        {log.activity} = {log.carbon} kg CO₂
                    </li>
                ))}
            </ul>
            <h2>Low Carbon Activities (≤ 4Kg)</h2>
            <ul>
                {lowImpactLogs.map((log) => (
                    <li style={{color:"#3a803cff"}} key={log.id}>
                        {log.activity} = {log.carbon} kg CO₂
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default Logs;