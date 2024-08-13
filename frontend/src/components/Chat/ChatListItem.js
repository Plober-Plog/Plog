import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const ChatListItem = ({key, chatRoom}) => {
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
    <div onClick={() => handleEnterChatRoom(chatRoom.chatRoom.chatRoomId, chatRoom.chatRoom.chatRoomName)}>
      <img src={chatRoom.users[0].image.imageUrl}/>
      <span>{chatRoom.chatRoom.chatRoomName}</span>
      <span>{chatRoom.lastChat.message}</span>
      <span>{chatRoom.lastChat.updatedAt}</span>
      <span>{chatRoom.lastChat.read}</span>
    </div>
  )

}

export default ChatListItem;