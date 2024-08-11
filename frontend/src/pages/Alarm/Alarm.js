import React, { useEffect, useState } from 'react';
import API from '../../apis/api';
import AlarmItem from '../../components/Alarm/AlarmItem';

const Alarm = () => {
  const [alarms, setAlarms] = useState([]);
  const [page, setPage] = useState(0);

  useEffect(() => {
    const fetchAlarms = async () => {
      try {
        const response = await API.get('/notification/history', {
          params: { page },
        });
        setAlarms(response.data);
        console.log(response.data)
      } catch (error) {
        console.error('Failed to fetch alarms:', error);
      }
    };

    fetchAlarms();
  }, []);

  return (
    <div>
      {alarms.length > 0 ? (
        <div>
          {alarms.map((alarm) => (
            <AlarmItem 
              alarm={alarm}
            />
          ))}
        </div>
      ) : (
        <p>알림이 없습니다</p>
      )}
    </div>
  );
};

export default Alarm;
