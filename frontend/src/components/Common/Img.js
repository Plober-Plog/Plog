import React from 'react';
import PropTypes from 'prop-types';

const Img = ({ src, alt, style }) => {
    return (
      <div>
        <img src={src} alt={alt} style={style} />
      </div>
    )
};

Img.propTypes = {
  src: PropTypes.string.isRequired,
  alt: PropTypes.string.isRequired,
};

export default Img;