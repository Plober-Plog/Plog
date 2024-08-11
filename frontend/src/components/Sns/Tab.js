import React, { useEffect } from 'react';
import Btn from '../../components/Common/Btn';
import './Tab.css';

const TabVisibilitySelector = ({ selectedVisibility, setSelectedVisibility }) => {

  useEffect(() => {
    console.log('공개상태:', selectedVisibility);
  }, [selectedVisibility]);

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
