import React from "react";
import NeighborCard from "./NeighborCard";

const NeighborCardList = ({ users }) => {
  return (
    <ul>
      {users.map((user) => (
        <NeighborCard
          key={user.searchId}
          user={user}
        />
      ))}
    </ul>
  )
}

export default NeighborCardList;