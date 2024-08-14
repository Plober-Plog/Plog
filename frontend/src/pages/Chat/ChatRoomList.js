import axios from 'axios';
import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCookie } from '../../utils/cookieUtils';
import ChatListItem from '../../components/Chat/ChatListItem';

const API_REALTIME_URL = "https://i11b308.p.ssafy.io/realtime";

const ChatRoomList = () => {
  const navigate = useNavigate();
  const [chatRooms, setChatRooms] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const token = getCookie('accessToken');  
  console.log('채팅방 토큰 : ', token);
  const containerRef = useRef(null);

  // 채팅방 불러오기 
  const fetchChatRooms = async () => {
    if (loading || !hasMore) return;

    setLoading(true);

    try {
      const response = await axios.get(`${API_REALTIME_URL}/chat/room`, {
        headers: {
          Authorization: token
        },
        params: {
          page: page,
          size: 10, // 한 번에 가져올 채팅방 수 (필요에 따라 조정)
        }
      });
      console.log('채팅방 불러오기 성공:', response.data);

      if (response.data.length === 0) {
        setHasMore(false);
      } else {
        // chatRoom.chatRoom.deleted가 false인 채팅방만 필터링
        const filteredChatRooms = response.data.filter(chatRoom => !chatRoom.chatRoom.deleted);
        setChatRooms((prevChatRooms) => [...prevChatRooms, ...filteredChatRooms]);
        setPage((prevPage) => prevPage + 1);
      }
    } catch (error) {
      console.error('채팅방 목록을 불러오는 중 오류 발생:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchChatRooms();
  }, []);

  // 스크롤 이벤트 핸들러
  const handleScroll = () => {
    if (containerRef.current) {
      const { scrollTop, scrollHeight, clientHeight } = containerRef.current;
      if (scrollTop + clientHeight >= scrollHeight - 5) { // 페이지의 끝에 도달하면
        fetchChatRooms();
      }
    }
  };

  useEffect(() => {
    const container = containerRef.current;
    if (container) {
      container.addEventListener('scroll', handleScroll);
      return () => {
        container.removeEventListener('scroll', handleScroll);
      };
    }
  }, [page, loading, hasMore]);

  return (
    <div className="chat-room-container" ref={containerRef} style={{ height: '80vh', overflowY: 'auto' }}>
      {chatRooms.length === 0 && !loading && <p>채팅이 없습니다</p>}
      {chatRooms.map((chatRoom) => (
        <ChatListItem
          key={chatRoom.chatRoom.chatRoomId}
          chatRoom={chatRoom}
          token={token}
        />
      ))}
      {loading && <p>Loading more chat rooms...</p>}
    </div>
  );
};

export default ChatRoomList;
