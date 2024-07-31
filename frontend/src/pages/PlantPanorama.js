import React, { useEffect, useState } from "react";

// 세부적인 것은 추후 수정 예정 (date 알고리즘 만들어진 뒤)
// plantId와 특정 date가 담긴 배열을 BE에 보내 이미지 요청
  // 이미지: 특정 date 일지의 대표 이미지
// FE는 수신한 이미지를 gifshot 라이브러리를 활용해 gif를 생성한 뒤
// 생성한 gif를 사용자에게 보여줘야 함

const PlantPanorama = ({ plantId }) => {

  const [gifUrl, setGifUrl] = useState(null);

  return (
    <div>
      <span>
        {gifUrl ? (
          <img src={gifUrl} />
        ) : (
          <p>Loading...</p>
        )}
      </span>
      <p>
        {plantId.birthDate}
      </p>
      <p>
        {plantId.nickname}
      </p>
    </div>
  )
};

export default PlantPanorama;