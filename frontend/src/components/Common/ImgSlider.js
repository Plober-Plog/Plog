import React from 'react';

// const ImageSlider = ({ images = [] }) => {
//   return (
//     <div>
//       {images.map((image, index) => (
//         <div key={index}>
//           <img src={image} alt={`slide-${index}`} />
//         </div>
//       ))}
//     </div>
//   );
// };

const ImageSlider = ({ images = [] }) => {
  return (
    <div>
      {images.length > 0 ? (
        images.map((image, index) => (
          <div key={index}>
            <img src={image} alt={`slide-${index}`} />
          </div>
        ))
      ) : (
        <div>
          <p>이미지가 없습니다</p>  {/* 이미지가 없는 걸 보려고 ? 씀*/ }
        </div>
      )}
    </div>
  );
};

export default ImageSlider;