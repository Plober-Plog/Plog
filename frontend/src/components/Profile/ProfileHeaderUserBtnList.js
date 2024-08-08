import React, {useState, useEffect} from 'react';
import useAuthStore from '../../stores/member';
import API from '../../apis/api';

const ProfileHeaderUserBtnList = ({ ownerId }) => {
  const authSearchId = useAuthStore((state) => state.getSearchId());

  const [profileUserRel, setProfileUserRel] = useState(null);
  const [requestUserRel, setRequestUserRel] = useState(null);

  const fetchRel = async () => {
    try {
      const response = await API.get(`/user/neighbor/${ownerId}`);
      setProfileUserRel(response.data.profileUserRel);
      setRequestUserRel(response.data.requestUserRel);
      // console.log('@@@ProfileUserRel:@@@', response.data);
    } catch (error) {
      console.error('@@@Rel Error:', error, '@@@');
    }
  };

  useEffect(() => {
    if (authSearchId) {
      fetchRel();
    }
  }, [authSearchId, ownerId]);

  const addNeighbor = async () => {
    try {
      await API.post('/user/neighbor', { neighborSearchId: ownerId });
      await fetchRel();
      console.log('이웃 추가 성공');
    } catch (error) {
      console.error('이웃 추가 실패:', error);
    }
  }

  const removeNeighbor = async () => {
    try {
      await API.delete('/user/neighbor', {
        headers: {
          'Content-Type': 'application/json',
        },
        data: { neighborSearchId: ownerId },
      });
      await fetchRel();
      console.log('이웃 삭제 성공');
    } catch (error) {
      console.error('이웃 삭제 실패:', error);
    }
  };


  if (profileUserRel === null || requestUserRel === null) {
    return null;
  }

  const getRequestUserRelText = () => {
    if (requestUserRel === 0) return `${ownerId}님은 당신을 이웃으로 두지 않습니다.`;
    if (requestUserRel === 1) return `${ownerId}님은 당신을 이웃으로 추가하고 있습니다.`;
    if (requestUserRel === 2) return `${ownerId}님은 당신과 서로이웃 입니다.`;
    return '';
  };

  return (
    <div>
      {authSearchId === ownerId && (
        <>
          <button style={{ margin: '10px' }}>🔖</button>
          <button style={{ margin: '10px' }}>이웃목록</button>
        </>
      )}
      {authSearchId !== ownerId && (
        <>
          <span>
            {getRequestUserRelText()}
          </span>
          <div>
            {profileUserRel === 0 && (
              <button style={{ margin: '10px' }} onClick={addNeighbor}>이웃 추가</button>
            )}
            {profileUserRel === 1 && (
              <>
                <button style={{ margin: '10px' }}>서로이웃 신청</button>
                <button style={{ margin: '10px' }} onClick={removeNeighbor}>이웃 취소</button>
              </>
            )}
            {profileUserRel === 2 && (
              <button style={{ margin: '10px' }}>서로이웃 끊기</button>
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default ProfileHeaderUserBtnList;