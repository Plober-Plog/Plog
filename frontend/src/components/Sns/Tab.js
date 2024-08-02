import React, { useState } from 'react';
import Btn from '../../components/Common/Btn';

const TabVisibilitySelector = () => {
  const [selectedVisibility, setSelectedVisibility] = useState(1); // 기본값을 "PUBLIC"으로 설정
  
  const handleTabVisibility = (value) => {
    setSelectedVisibility(value);
  };

  // 버튼 색 변경 일단 임시입니다 ... 추후 수정 가능성 높습니다.  
  const buttonStyle = (isActive) => ({
    backgroundColor: isActive ? '#87B989' : '#f0f0f0',
    color: isActive ? 'white' : 'black',
    padding: '10px 20px',
    border: 'none',
    cursor: 'pointer',
    margin: '0px'
  });

  return (
    <div className="visibility-selector">
      <Btn
        content="전체 공개"
        onClick={() => handleTabVisibility(1)}
        style={buttonStyle(selectedVisibility === 1)}
      />
      <Btn
        content="이웃 공개"
        onClick={() => handleTabVisibility(2)}
        style={buttonStyle(selectedVisibility === 2)}
      />
      <Btn
        content="서로이웃 공개"
        onClick={() => handleTabVisibility(3)}
        style={buttonStyle(selectedVisibility === 3)}
      />
      {/* <Btn
        content="나만 보기"
        onClick={() => handleTabVisibility(4)}
        style={buttonStyle(selectedVisibility === 4)}
      /> */}
    </div>
  );
};

export default TabVisibilitySelector;



