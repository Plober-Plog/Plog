
import { useNavigate } from "react-router-dom";
import useButtonStore from "../../stores/footer";
import snsSelect from "../../assets/icon/footer/sns-select.png"

const Footer = () => {
  const { activeButton, setActiveButton } = useButtonStore(); 
  const navigate = useNavigate();

  const handleClick = (icon) => {
    setActiveButton(icon);

    switch (icon) {
      case "sns":
        navigate("/sns");
        break;
      case "profile":
        navigate("/profile");
        break;
      case "home":
        navigate("/");
        break;
      case "chat":
        navigate("/chat");
        break;
      case "alarm":
        navigate("/alarm");
        break;
      default:
        break;
    }
  };

  return (
    <div>
      <img
        src={activeButton === "sns" ? "../../assets/icon/footer/sns-select.png" : "../../assets/icon/footer/sns-default.png"}
        alt="sns 아이콘"
        onClick={() => handleClick("sns")}
      />
      <img
        src={activeButton === "profile" ? "../../assets/icon/footer/profile-select.png" : "../../assets/icon/footer/profile-default.png"}
        alt="profile 아이콘"
        onClick={() => handleClick("profile")}
      />
      <img
        src={activeButton === "home" ? "../../assets/icon/footer/home-select.png" : "../../assets/icon/footer/home-default.png"}
        alt="home 아이콘"
        onClick={() => handleClick("home")}
      />
      <img
        src={activeButton === "chat" ? "../../assets/icon/footer/chat-select.png" : "../../assets/icon/footer/chat-default.png"}
        alt="chat 아이콘"
        onClick={() => handleClick("chat")}
      />
      <img
        src={activeButton === "alarm" ? "../../assets/icon/footer/alarm-select.png" : "../../assets/icon/footer/alarm-default.png"}
        alt="alarm 아이콘"
        onClick={() => handleClick("alarm")}
      />
    </div>
  );
};

export default Footer;