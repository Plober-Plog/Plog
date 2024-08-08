import React from 'react';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

const ImgSlider = ({ imgs = [] }) => {
  const settings = {
    dots: true,
    infinite: false,
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: true,
    autoplay: false // 자동 슬라이드 비활성화
  };

  return (
    <div>
      {imgs.length > 0 ? (
        <Slider {...settings}>
          {imgs.map((img, index) => (
            <div key={index}>
              <img src={img} alt={`slide-${index}`} style={{ width: '300px', height: '300px' }} />
            </div>
          ))}
        </Slider>
      ) : (
        <div>
          <p>이미지가 없습니다</p>  {/* 이미지가 없는 걸 보려고 ? 씀*/ }
        </div>
      )}
    </div>
  );
};

export default ImgSlider;
