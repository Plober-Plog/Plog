import React, { useEffect } from 'react';
import Btn from '../../components/Common/Btn';
import './Tab.css';

const TabVisibilitySelector = ({ selectedVisibility, setSelectedVisibility }) => {
  // 변경 값 확인
  useEffect(() => {
    console.log('공개상태:', selectedVisibility);
  }, [selectedVisibility]);

  // 버튼 색 변경 일단 임시입니다 ... 추후 수정 가능성 높습니다.  
  // const buttonStyle = (isActive) => ({
  //   backgroundColor: isActive ? '#87B989' : '#f0f0f0',
  //   color: isActive ? 'white' : 'black',
  //   padding: '10px 20px',
  //   border: 'none',
  //   cursor: 'pointer',
  //   margin: '0px'
  // });

  return (
    <div className="w-screen max-w-none visibility-selector">
      <button
        onClick={() => setSelectedVisibility(1)}
        className={selectedVisibility === 1 ? 'active' : ''}
      >
        전체 공개
      </button>
      <button
        onClick={() => setSelectedVisibility(2)}
        className={selectedVisibility === 2 ? 'active' : ''}
      >
        이웃 공개
      </button>
    </div>
  );
};

export default TabVisibilitySelector;
