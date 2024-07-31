import ProfileHeader from "../components/Profile/ProfileHeader";
import Calander from "../components/Plant/Calander";
import ArticleCardList from "../components/Article/ArticleCardList";
import ReportBanner from "../components/Plant/ReportBanner";
import Btn from "../components/Common/Btn";


const PlantDetail = () => {

  // 가라 데이터... API 만들어지면 수정
  const plantData = {
    profile: "",
    plantTypeId: "튤립",
    nickname: "조이",
    bio: "행복한 조이"
  }

  return (
    <div>
      <ProfileHeader
        data={plantData}
        type="plant"
      />
      <Calander />
      <ArticleCardList />
      <ReportBanner />
      <Btn content ="성장과정 보기"/>
      <Btn content ="가이드"/>
    </div>
  )
};

export default PlantDetail;