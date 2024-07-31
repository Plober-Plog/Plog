import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';


const WriterInfo = ({ data = {}, type }) => {
  const defaultImg = '../../assets/icon/default.png';
  const writerInfoData = type === 'plant' ? {
    img: data.profile || defaultImg,
    nickname: data.nickname || 'Unknown Plant',
    info: data.plantTypeId || 'Unknown Nickname',
  } : {
    img: data.profile || defaultImg,
    nickname: data.nickname || 'Unknown Nickname',
    info: data.recordDate || 'Unknown Date',
  };

  return (
    <div>
      <div>
        <img src={writerInfoData.img} alt='profile img' />
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