import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ImgSlider from '../../components/Common/ImgSlider';
import DiaryDetailContent from '../../components/Diary/DiaryDetailContent';
import html2canvas from 'html2canvas';
import API from '../../apis/api';
import './PlantReport.css'; 
import Btn from '../../components/Common/Btn';

const PlantReport = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { plantId } = location.state;
  const reportRef = useRef(null);
  const [plantData, setPlantData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPlantReportData = async () => {
      try {
        const response = await API.get(`/user/report/${plantId}`, {
          plantId: plantId,
        });

        console.log(response.data);
        setPlantData(response.data);
        setLoading(false);
      } catch (error) {
        setLoading(false);
      }
    };

    fetchPlantReportData();
  }, [plantId]);

  if (loading) return <p>로딩중입니당..</p>;

  if (!plantData) return <p>데이터가 없습니다.</p>;

  // 일반 이미지 URL
  const normalUrl1 = `${plantData.firstDayImageUrl}?t=${new Date().getTime()}`;
  const normalUrl2 = `${plantData.recentImageUrl}?t=${new Date().getTime()}`;
  const imageUrls = [normalUrl1, normalUrl2];

  // 캡처를 위한 프록시 URL
  const proxyUrl = (s3Url) => `https://i11b308.p.ssafy.io/api/image/proxy?url=${encodeURIComponent(s3Url)}`;
  const proxyUrl1 = proxyUrl(normalUrl1);
  const proxyUrl2 = proxyUrl(normalUrl2);
  const proxyImageUrls = [proxyUrl1, proxyUrl2];

  const reportContent = `기간 동안 물 준 횟수 ${plantData.fertilizeData}번 💧\n 
  기간 동안 영양제 준 횟수 ${plantData.fertilizeData}번💊\n
  기간 동안 분갈이 횟수 ${plantData.repotData}번🌱\n
  \n
  물주기 점수는 "${plantData.waterResult}"\n
  영양제 점수는 "${plantData.fertilizeResult}"\n
  분갈이 점수는 "${plantData.repoResult}"\n 
  \n
  앞으로도 ${plantData.plantName} 잘 돌봐주실 거죠 😊?\n `

  // 캡쳐링 부분
  const handleCapture = async () => {
    if (reportRef.current) {
        try {
            // 캡처를 위한 프록시 URL을 임시로 이미지에 적용
            const imgElements = reportRef.current.querySelectorAll('img');
            const originalUrls = [];
            imgElements.forEach((img, index) => {
                originalUrls.push(img.src); // 원래 URL 저장
                img.src = proxyImageUrls[index]; // 프록시 URL 적용
                console.log(`For capture - Image ${index + 1} URL: `, img.src); // 프록시 URL 출력
            });

            // 약간의 지연을 두어 이미지가 실제로 로드되도록 함
            await new Promise(resolve => setTimeout(resolve, 100));

            const canvas = await html2canvas(reportRef.current, { useCORS: true });
            const reportImgData = canvas.toDataURL('image/png');

            // base64 문자열을 Blob으로 변환
            const byteString = atob(reportImgData.split(',')[1]);
            const mimeString = reportImgData.split(',')[0].split(':')[1].split(';')[0];
            const ab = new ArrayBuffer(byteString.length);
            const ia = new Uint8Array(ab);
            for (let i = 0; i < byteString.length; i++) {
                ia[i] = byteString.charCodeAt(i);
            }
            const blob = new Blob([ab], { type: mimeString });
            const file = new File([blob], 'report.png', { type: mimeString });

            // 파일 객체를 배열로 감싸서 `navigate`로 전달
            navigate('/sns/write', { state: { reportImgData: [{ url: URL.createObjectURL(file), file }], articleId: 0 } });

            // 캡처 후 다시 원래 URL로 복원
            imgElements.forEach((img, index) => {
                img.src = originalUrls[index];
                console.log(`After capture - Image ${index + 1} URL restored: `, img.src); // 원래 URL로 복원 후 출력
            });

        } catch (error) {
            console.error('이미지 캡처 중 오류 발생:', error);
        }
    }
};



  return (
    <div className="plant-report-container" ref={reportRef}>
      <div className="cardlist-subtitle mb-4">
        <h2>{plantData.plantName} 분석보고서</h2>
      </div>
      <div className="plant-report-slider">
        <ImgSlider imgs={imageUrls} /> {/* 일반 이미지를 ImgSlider에 전달 */}
      </div>
      <div className="plant-report-name">
        <p>식물 이름: {plantData.plantName}</p>
      </div>
      <div className="plant-report-details">
        <DiaryDetailContent detailContent={reportContent}/>
      </div>
      <div className='plant-report-sns-btn'>
        <Btn content='SNS 공유하기' onClick={handleCapture} /> 
      </div>
    </div>
  );
};

export default PlantReport;
