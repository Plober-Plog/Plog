import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import './WriterInfo.css';

const WriterInfo = ({ data = {}, type }) => {
  const defaultImg = '../../assets/icon/default.png';
  const writerInfoData = {}

  if (type === 'plant') {
    writerInfoData.imgSrc = data.profile || defaultImg;
    writerInfoData.nickname = data.nickname || 'Unknown Nickname';
    writerInfoData.info = data.plantTypeName || data.otherPlantTypeName;
  } else if (type === 'user') {
    writerInfoData.imgSrc = data.profile || defaultImg;
    writerInfoData.nickname = data.nickname || 'Unknown Nickname';
    writerInfoData.info = data.recordDate || 'Unknown Date';
  } else {
    console.error(`Unexpected type: ${type}`)
  }

  return (
    <div className="writerInfo-container">
      <div>
        <img src={writerInfoData.imgSrc} alt='profile img' className="writerInfo-profile-pic" />
      </div>
      <div className="writerInfo-text">
        <div className="writerInfo-nickname">
          {writerInfoData.nickname}
        </div>
        <div className="writerInfo-info">
          {writerInfoData.info}
        </div>
      </div>
    </div>
  );
};

WriterInfo.propTypes = {
  data: PropTypes.object.isRequired,
  type: PropTypes.string.isRequired,
};

export default WriterInfo;
