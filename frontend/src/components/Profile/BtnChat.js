import React from "react";
import axios from 'axios';
import { getCookie } from '../../utils/cookieUtils';
import { useNavigate } from 'react-router-dom';
import chatIcon from '../../assets/icon/chat-white.png';

const BtnChat = ({ userData }) => {
  const API_REALTIME_URL = process.env.REACT_APP_API_REALTIME_URL;
  const token = getCookie('accessToken');  
  const navigate = useNavigate();

  const handleChat = async () => {
    console.log(API_REALTIME_URL)
    try {
      console.log(API_REALTIME_URL)
      const response = await axios.post(`${API_REALTIME_URL}/chat/room`, {
        targetSearchId: userData.searchId,
        chatRoomType: 1,
        chatRoomName: userData.nickname,
      }, {
        headers: {
          Authorization: token
        }
      });
      console.log('새 채팅방 개설:', response.data.message);
      
      const chatRoomId = response.data.message;
      navigate(`/chat/${chatRoomId}`, {
        state: {
          chatRoomId: chatRoomId,
        }
      });
    } catch (error) {
      if (error.response && error.response.status === 409) {
        console.log('이미 존재하는 채팅방으로 이동합니다:', error.response.data.message);
        const chatRoomId = error.response.data.message;
        navigate(`/chat/${chatRoomId}`, {
          state: {
            chatRoomId: chatRoomId,
          }
        });
      } else {
        console.error('채팅방 개설 중 오류 발생:', error);
      }
    }
  };

  return (
    <button className="add-btn" onClick={handleChat}>
      <img src={chatIcon} alt="채팅 아이콘" />
    </button>
  ) 
}

export default BtnChat;
