<<<<<<< HEAD
import React from 'react';

/* TODO: 추후 유저 프로필을 구현하게 될 때
  요청 유저와 페이지 유저의 관계를 체크 후 관계에 따라 다르게 렌더해야 함

  1. 본인일 경우 : 북마크 아이콘
  2. 이웃이 아닐 경우 : 이웃 신청 Btn
  3. 이웃일 경우 : 서로이웃 신청 Btn, 이웃 취소 Btn
  4. 서로이웃일 경우 : 이웃 취소 Btn
    (추후 모달로 이웃으로 전환, 완전히 이웃 끊기 선택지 제공)
*/

const ProfileHeaderUserBtnList = ({}) => {
  return (
    <div>
      <button style={{ margin: '10px' }}>이웃 신청</button>
      <button style={{ margin: '10px' }}>서로이웃 신청</button>
      <button style={{ margin: '10px' }}>이웃 끊기</button>
=======
import React, {useState, useEffect} from 'react';
import { useNavigate } from'react-router-dom';
import useAuthStore from '../../stores/member';
import API from '../../apis/api';
import './ProfileHeaderUserBtnList.css'

const ProfileHeaderUserBtnList = ({ ownerId }) => {
  const authSearchId = useAuthStore((state) => state.getSearchId());
  const navigate = useNavigate();
  const [profileUserRel, setProfileUserRel] = useState(null);
  const [requestUserRel, setRequestUserRel] = useState(null);

  const fetchRel = async () => {
    try {
      const response = await API.get(`/user/neighbor/${ownerId}`);
      setProfileUserRel(response.data.profileUserRel);
      setRequestUserRel(response.data.requestUserRel);
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
    } catch (error) {
      console.error('이웃 삭제 실패:', error);
    }
  };


  if (profileUserRel === null || requestUserRel === null) {
    return null;
  }

  const handleClickNeighborList = () => {
    navigate(`/profile/${ownerId}/neighbor`)
  }

  return (
    <div className="profile-header-btn-container">
      {authSearchId === ownerId && (
        <>
          <button 
            className="profile-header-btn-small"
            onClick={handleClickNeighborList}
          >
            이웃목록
          </button>
        </>
      )}
      {authSearchId !== ownerId && (
        <>
          <div>
            {profileUserRel === 0 && (
              <button
                className="profile-header-btn-small"
                onClick={addNeighbor}>
                이웃 추가
              </button>
            )}
            {profileUserRel === 1 && (
              <>
                <button
                  className="profile-header-btn-small"
                  onClick={removeNeighbor}>
                  이웃 취소
                </button>
              </>
            )}
          </div>
        </>
      )}
>>>>>>> master
    </div>
  );
};

export default ProfileHeaderUserBtnList;