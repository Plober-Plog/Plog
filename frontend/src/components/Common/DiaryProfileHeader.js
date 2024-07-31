import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';


const DiaryProfileHeader = ({ data = {}, type }) => {
  const diaryProfileData = type === 'plant' ? {
    img: data.profile || '',
    nickname: data.nickname || 'Unknown Plant',
    info: data.plantTypeId || 'Unknown Nickname',
  } : {
    img: data.profile || '',
    nickname: data.nickname || 'Unknown Nickname',
    info: data.recordDate || 'Unknown Date',
  };

  return (
    <div>
      <div>
        <img src={diaryProfileData.img} alt='profile img' />
      </div>
      <div>
        {diaryProfileData.nickname}
      </div>
      <div>
        {diaryProfileData.info}
      </div>
    </div>
  )};

DiaryProfileHeader.propTypes = {
  data: PropTypes.object.isRequired,
  type: PropTypes.string.isRequired,
};

export default DiaryProfileHeader;