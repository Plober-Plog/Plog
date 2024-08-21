import React from 'react';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import './ImgSlider.css'; 

const ImgSlider = ({ imgs = [] }) => {
  const settings = {
    dots: true,
    infinite: false,
    slidesToShow: 1, // 한 번에 한 장의 이미지만 보여주기
    slidesToScroll: 1,
    arrows: true,
    autoplay: false, // 자동 슬라이드 비활성화
    adaptiveHeight: true // 이미지에 따라 슬라이더의 높이가 조정됨
  };

  return (
    <div className="img-slider-container">
      {imgs.length > 0 ? (
        <Slider {...settings}>
          {imgs.map((img, index) => (
            <div key={index} className="slide">
              <img src={img} alt={`slide-${index}`} className="slide-img" />
            </div>
          ))}
        </Slider>
      ) : (
        <div>
          <p>이미지가 없습니다</p>
        </div>
      )}
    </div>
  );
};

export default ImgSlider;
