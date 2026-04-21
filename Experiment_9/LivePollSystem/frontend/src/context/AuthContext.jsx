import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { apiRequest } from "../api/client";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("livepoll_token") || "");
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem("livepoll_user");
    return raw ? JSON.parse(raw) : null;
  });

  useEffect(() => {
    if (token) {
      localStorage.setItem("livepoll_token", token);
    } else {
      localStorage.removeItem("livepoll_token");
    }
  }, [token]);

  useEffect(() => {
    if (user) {
      localStorage.setItem("livepoll_user", JSON.stringify(user));
    } else {
      localStorage.removeItem("livepoll_user");
    }
  }, [user]);

  const value = useMemo(
    () => ({
      token,
      user,
      isAuthenticated: Boolean(token),
      setSession: (nextToken, nextUser) => {
        setToken(nextToken);
        setUser(nextUser);
      },
      clearSession: () => {
        setToken("");
        setUser(null);
      },
      refreshMe: async () => {
        const me = await apiRequest("/api/auth/me", { method: "GET" }, token);
        setUser(me);
      }
    }),
    [token, user]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return ctx;
}
