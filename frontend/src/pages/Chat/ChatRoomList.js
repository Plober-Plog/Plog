// import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import REALTIME_API from '../../apis/api';

// const API_REALTIME_URL = "https://i11b308.p.ssafy.io/realtime";
// const token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjQwNDUzMTUsImlhdCI6MTcyMjgzNTcxNX0.BFVSXUtidgN3jrayov5V0wnDeU2QkItay86uQu2wf3o";

const ChatRooms = () => {
  const [chatRooms, setChatRooms] = useState([]);
  const navigate = useNavigate();
  const fetchChatRooms = async () => {
    try {
      const response = await REALTIME_API.get(`/chat/room`);
      console.log(response);
      setChatRooms(response.data);
    } catch (error) {
      console.error('채팅방 목록을 불러오는 중 오류 발생:', error);
    }
  };
  useEffect(() => {
    

    fetchChatRooms();
  }, []);

  const handleCreateChatRoom = async () => {
    try {
      const response = await REALTIME_API.post(`/chat/room`, {
        chatRoomType: 1,
        chatRoomName: "테스트"
      },);
      console.log('새 채팅방 개설:', response.data);
      fetchChatRooms(); // 새로운 채팅방 생성 후 목록 갱신
    } catch (error) {
      console.error('채팅방 개설 중 오류 발생:', error);
    }
  };

  const handleEnterChatRoom = (chatRoomId) => {
    navigate(`/chat/${chatRoomId}`);
  };

  return (
    <div className="chat-room-container">
      <h1>채팅방 목록</h1>
      <ul>
        {chatRooms.map((room) => (
          <li key={room.chatRoomId} onClick={() => handleEnterChatRoom(room.chatRoomId)}>
            {room.chatRoomName}
          </li>
        ))}
      </ul>
      <button onClick={handleCreateChatRoom}>채팅방 개설</button>
    </div>
  );
};

export default ChatRooms;
