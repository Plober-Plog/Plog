import React from "react";
import NeighborCard from "./NeighborCard";

const NeighborCardList = ({ users }) => {
  return (
    <ul className="grid grid-cols-12 gap-4">
      {users.map((user) => (
        <NeighborCard
          key={user.searchId}
          user={user}
          // className='col-span-4'
        />
      ))}
    </ul>
  )
}

export default NeighborCardList;