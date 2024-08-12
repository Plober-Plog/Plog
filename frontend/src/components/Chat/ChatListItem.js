import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const ChatListItem = ({key, chatRoomId, chatRoomName}) => {
  const navigate = useNavigate();

  const handleEnterChatRoom = (chatRoomId, chatRoomName) => {
    navigate(`/chat/${chatRoomId}`, {
      state: {
        chatRoomId,
        otherUserNickname: chatRoomName,
      }
    });
  };

  return (
    <div onClick={() => handleEnterChatRoom(chatRoomId, chatRoomName)}>
      {/* 상대방 프사<img src={}/> */}
      <span>{chatRoomName}</span>
      {/* 상대방 마지막 대화 내용 */}
      {/* 상대방 마지막 대화 시간 */}
    </div>
  )

}

export default ChatListItem;