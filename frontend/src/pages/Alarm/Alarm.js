import React, { useEffect, useState } from 'react';
import axios from 'axios';
import AlarmItem from '../../components/Alarm/AlarmItem';
import useAuthStore from '../../stores/member';
import { format, isToday, isYesterday, subDays } from 'date-fns';
import './Alarm.css';

const Alarm = () => {
  const [groupedAlarms, setGroupedAlarms] = useState({});
  const [page, setPage] = useState(0);
  const [hasMoreAlarms, setHasMoreAlarms] = useState(true); // 추가된 상태
  const searchId = useAuthStore((state) => state.getSearchId());
  const API_REALTIME_URL = process.env.REACT_APP_REALTIME_BASE_URL;

  useEffect(() => {
    const fetchAlarms = async () => {
      try {
        const response = await axios.get(`${API_REALTIME_URL}/notification/history`, {
          params: { searchId, page },
        });
        console.log(response.data)
        if (response.data.length === 0) {
          setHasMoreAlarms(false);
        } else {
          const alarmsByDate = response.data.reduce((groups, alarm) => {
            const date = alarm.notificationDate;
            if (!groups[date]) {
              groups[date] = [];
            }
            groups[date].push(alarm);
            return groups;
          }, {});

          setGroupedAlarms((prevAlarms) => {
            const mergedAlarms = { ...prevAlarms };
            Object.keys(alarmsByDate).forEach((date) => {
              if (!mergedAlarms[date]) {
                mergedAlarms[date] = [];
              }
              mergedAlarms[date] = [...mergedAlarms[date], ...alarmsByDate[date]];
            });
            return mergedAlarms;
          });

          console.log('날짜별로 그룹화된 알림 내역: ', alarmsByDate);
        }
      } catch (error) {
        console.error('알림 받아오기 실패:', error);
      }
    };

    if (hasMoreAlarms) {
      fetchAlarms();
    }
  }, [page]); // page가 변경될 때마다 알림을 가져옴

  
  
  // 스크롤 이벤트로 페이지 증가
  useEffect(() => {
    const handleScroll = () => {
      if (window.innerHeight + document.documentElement.scrollTop >= document.documentElement.offsetHeight && hasMoreAlarms) {
        setPage((prevPage) => prevPage + 1);
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [hasMoreAlarms]);


  // 날짜 포맷팅 함수
  const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    
    if (isToday(date)) {
      return '오늘';
    } else if (isYesterday(date)) {
      return '어제';
    } else if (isSameDay(date, subDays(new Date(), 2))) {
      return '그저께';
    } else {
      return format(date, 'yyyy년 MM월 dd일');
    }
  };

  // 날짜 가져오기
  const isSameDay = (date1, date2) => {
    return (
      date1.getFullYear() === date2.getFullYear() &&
      date1.getMonth() === date2.getMonth() &&
      date1.getDate() === date2.getDate()
    );
  };

  return (
    <div className='alarm-container'>
      {Object.keys(groupedAlarms).length > 0 ? (
        Object.keys(groupedAlarms).map((date) => (
          <div key={date} className='alarm-group-box'>
            <h3 className='alarm-date'>{formatDate(date)}</h3>
            {groupedAlarms[date].map((alarm) => (
              <AlarmItem 
                key={alarm.notificationId} 
                alarm={alarm}
              />
            ))}
            <hr className='alarm-hr'/>
          </div>
        ))
      ) : (
        <p>알림이 없습니다</p>
      )}
    </div>
  );
};

export default Alarm;
