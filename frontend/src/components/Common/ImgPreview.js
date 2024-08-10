import React from 'react';
import PropTypes from 'prop-types';
import Btn from './Btn'


const ImgPreview = ({ src, onDelete }) => {
  return (
    <div style={{ display: 'inline-block', margin: '5px' }}>
      <img src={src} alt="preview" style={{ width: '70px', height: '70px', objectFit: 'cover' }} />
      <div>
        <Btn 
          content="X" 
          type="button" 
          onClick={onDelete}

        />
      </div>
    </div>
  );
};

ImgPreview.propTypes = {
  src: PropTypes.string.isRequired,
  onDelete: PropTypes.func.isRequired,
};


export default ImgPreview;
