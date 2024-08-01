import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import '../../pages/Diary/PlantDiaryWrite.css';

const WriterInfo = ({ data = {}, type }) => {
  const defaultImg = '../../assets/icon/default.png';
  const writerInfoData = {}

  if (type === 'plant') {
    writerInfoData.imgSrc = data.profile || defaultImg;
    writerInfoData.nickname = data.nickname || 'Unknown Nickname';
    writerInfoData.info = data.plantTypeId || 'Unknown Plant';
  } else if (type === 'user') {
    writerInfoData.imgSrc = data.profile || defaultImg;
    writerInfoData.nickname = data.nickname || 'Unknown Nickname';
    writerInfoData.info = data.recordDate || 'Unknown Date';
  } else {
    console.error(`Unexpected type: ${type}`)
  }
  

  return (
    <div>
      <div>
        <img src={writerInfoData.imgSrc} alt='profile img' className="profile-pic" />
      </div>
      <div>
        {writerInfoData.nickname}
      </div>
      <div>
        {writerInfoData.info}
      </div>
    </div>
  )};

  WriterInfo.propTypes = {
  data: PropTypes.object.isRequired,
  type: PropTypes.string.isRequired,
};

export default WriterInfo;