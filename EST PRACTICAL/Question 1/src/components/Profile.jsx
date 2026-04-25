import React, { useContext } from "react";
import UserContext from "../UserContext";

function Profile() {
  const { username, isLoggedIn } = useContext(UserContext);

  return (
    <div>
      <h3>Profile</h3>

      <p><strong>Status:</strong> {isLoggedIn ? "Logged In ✅" : "Not Logged In ❌"}</p>

      {isLoggedIn && <p><strong>Username:</strong> {username}</p>}
    </div>
  );
}

export default Profile;