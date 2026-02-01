
import {BrowserRouter,Routes,Route,Navigate} from "react-router-dom";
import Header from "./components/Header.jsx";
import Dashboard from "./pages/dashboard.jsx";
import Logs from "./pages/Logs.jsx";
import Login from "./pages/Login.jsx";
import ProtectedRoutes from "./routes/ProtectedRoutes.jsx";
import DashboardLayout from "./pages/DashboardLayout.jsx";
import DashboardAnalytics from "./pages/DashboardAnalytics.jsx";
import DashboardSummary from "./pages/DashboardSummary.jsx";

function App() {
  return (
    <BrowserRouter>
      <Header title="Ecotrack"/>
      <Routes>
        <Route path="/" element={<Navigate to="/Login"/>}/>
        <Route path="/login" element={<Login/>}/>
        <Route path="/" element={
          <ProtectedRoutes>
            <DashboardLayout/>
          </ProtectedRoutes>
        }>
          <Route index element={<DashboardSummary/>}/>
          <Route path="summary" element={<DashboardSummary/>}/>
          <Route path="analytics" element={<DashboardAnalytics/>}/>
          <Route path="logs" element={<ProtectedRoutes> <Logs/></ProtectedRoutes>}/>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;