import React, { useEffect, useState } from 'react';
import axios from 'axios';
import AlarmItem from '../../components/Alarm/AlarmItem';
import useAuthStore from '../../stores/member';
import { format, isToday, isYesterday, subDays } from 'date-fns';

const Alarm = () => {
  const [groupedAlarms, setGroupedAlarms] = useState({});
  const [page, setPage] = useState(0);
  const searchId = useAuthStore((state) => state.getSearchId());

  useEffect(() => {
    const fetchAlarms = async () => {
      try {
        const response = await axios.get('https://i11b308.p.ssafy.io/realtime/notification/history', {
          params: { searchId, page },
        });
        
        // 알림을 날짜별로 그룹화
        const alarmsByDate = response.data.reduce((groups, alarm) => {
          const date = alarm.notificationDate;
          if (!groups[date]) {
            groups[date] = [];
          }
          groups[date].push(alarm);
          return groups;
        }, {});

        setGroupedAlarms(alarmsByDate);
        console.log('날짜별로 그룹화된 알림 내역: ', alarmsByDate);
      } catch (error) {
        console.error('알림 받아오기 실패:', error);
      }
    };

    fetchAlarms();
  }, []);

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
    <div>
      {Object.keys(groupedAlarms).length > 0 ? (
        Object.keys(groupedAlarms).map((date) => (
          <div key={date}>
            <h3>{formatDate(date)}</h3>
            {groupedAlarms[date].map((alarm) => (
              <AlarmItem 
                key={alarm.notificationId} 
                alarm={alarm}
              />
            ))}
          <hr />
          </div>
        ))
      ) : (
        <p>알림이 없습니다</p>
      )}
    </div>
  );
};

export default Alarm;
