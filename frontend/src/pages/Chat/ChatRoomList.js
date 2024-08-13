import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCookie } from '../../utils/cookieUtils';

import ChatListItem from '../../components/Chat/ChatListItem';

const API_REALTIME_URL = "https://i11b308.p.ssafy.io/realtime";

const ChatRoomList = () => {
  const navigate = useNavigate();
  const [chatRooms, setChatRooms] = useState([]);
  const token = getCookie('accessToken');  
  console.log('채팅방 토큰 : ', token);
  

  // 채팅방 불러오기 
  const fetchChatRooms = async () => {
    try {
      const response = await axios.get(`${API_REALTIME_URL}/chat/room`, {
        headers: {
          Authorization: token
        }
      });
      console.log('채팅방 불러오기 성공:', response.data);
      setChatRooms(response.data);
    } catch (error) {
      console.error('채팅방 목록을 불러오는 중 오류 발생:', error);
    }
  };

  useEffect(() => {
    
    fetchChatRooms();
  }, []);



  return (
    <div className="chat-room-container">
      {chatRooms.map((chatRoom) => (
        <ChatListItem
          key={chatRoom.chatRoomId}
          chatRoom={chatRoom}
        />
      ))}
      {/* <button onClick={handleCreateChatRoom}>채팅방 개설</button> */}
    </div>
  );
};

export default ChatRoomList;
