import React from 'react';
import PropTypes from 'prop-types';

<<<<<<< HEAD
const Img = ({ src, alt, style }) => {
    return (
      <div>
        <img src={src} alt={alt} style={style} />
      </div>
    )
=======
const Img = ({ src, alt }) => {
    const imgStyle = {
        width: '100%',
        height: '100%',
        borderRadius: '10px',
        objectFit: 'cover',
    };

    const containerStyle = {
        width: '100%',
        maxWidth: '600px',
        margin: '0 auto',
        paddingTop: '100%',
        position: 'relative',
    };

    const imageWrapperStyle = {
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
    };

    return (
        <div style={containerStyle}>
            <div style={imageWrapperStyle}>
                <img src={src} alt={alt} style={imgStyle} />
            </div>
        </div>
    );
>>>>>>> master
};

Img.propTypes = {
  src: PropTypes.string.isRequired,
  alt: PropTypes.string.isRequired,
};

<<<<<<< HEAD
export default Img;
=======
export default Img;
>>>>>>> master
