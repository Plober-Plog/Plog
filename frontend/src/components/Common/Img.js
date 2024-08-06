import React from 'react';
import PropTypes from 'prop-types';

const Img = ({ src, alt }) => {
    return (
      <div>
        <img src={src} alt={alt} />
      </div>
    )
};

Img.propTypes = {
  src: PropTypes.string.isRequired,
  alt: PropTypes.string.isRequired,
};

export default Img;