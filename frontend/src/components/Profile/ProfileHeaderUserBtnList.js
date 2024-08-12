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
    </div>
  );
};

export default ProfileHeaderUserBtnList;