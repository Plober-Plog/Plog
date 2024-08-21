import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
<<<<<<< HEAD
import '../../pages/Diary/PlantDiaryWrite.css';
=======
import './WriterInfo.css';
>>>>>>> master

const WriterInfo = ({ data = {}, type }) => {
  const defaultImg = '../../assets/icon/default.png';
  const writerInfoData = {}

  if (type === 'plant') {
    writerInfoData.imgSrc = data.profile || defaultImg;
    writerInfoData.nickname = data.nickname || 'Unknown Nickname';
<<<<<<< HEAD
    writerInfoData.info = data.plantTypeId || 'Unknown Plant';
=======
    writerInfoData.info = data.plantTypeName || data.otherPlantTypeName;
>>>>>>> master
  } else if (type === 'user') {
    writerInfoData.imgSrc = data.profile || defaultImg;
    writerInfoData.nickname = data.nickname || 'Unknown Nickname';
    writerInfoData.info = data.recordDate || 'Unknown Date';
  } else {
    console.error(`Unexpected type: ${type}`)
  }
<<<<<<< HEAD
  

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
=======

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
>>>>>>> master
  data: PropTypes.object.isRequired,
  type: PropTypes.string.isRequired,
};

<<<<<<< HEAD
export default WriterInfo;
=======
export default WriterInfo;
>>>>>>> master
