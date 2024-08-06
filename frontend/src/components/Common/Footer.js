import { useNavigate } from "react-router-dom";
import useButtonStore from "../../stores/footer";
import snsSelect from "../../assets/icon/footer/sns-select.png";
import snsDefault from "../../assets/icon/footer/sns-default.png";
import profileSelect from "../../assets/icon/footer/profile-select.png";
import profileDefault from "../../assets/icon/footer/profile-default.png";
import homeSelect from "../../assets/icon/footer/home-select.png";
import homeDefault from "../../assets/icon/footer/home-default.png";
import chatSelect from "../../assets/icon/footer/chat-select.png";
import chatDefault from "../../assets/icon/footer/chat-default.png";
import alarmSelect from "../../assets/icon/footer/alarm-select.png";
import alarmDefault from "../../assets/icon/footer/alarm-default.png";

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
        src={activeButton === "sns" ? snsSelect : snsDefault}
        alt="sns 아이콘"
        onClick={() => handleClick("sns")}
        style={{ width: '50px', height: '50px'}}
      />
      <img
        src={activeButton === "profile" ? profileSelect : profileDefault}
        alt="profile 아이콘"
        onClick={() => handleClick("profile")}
        style={{ width: '50px', height: '50px'}}
      />
      <img
        src={activeButton === "home" ? homeSelect : homeDefault}
        alt="home 아이콘"
        onClick={() => handleClick("home")}
        style={{ width: '50px', height: '50px'}}
      />
      <img
        src={activeButton === "chat" ? chatSelect : chatDefault}
        alt="chat 아이콘"
        onClick={() => handleClick("chat")}
        style={{ width: '50px', height: '50px'}}
      />
      <img
        src={activeButton === "alarm" ? alarmSelect : alarmDefault}
        alt="alarm 아이콘"
        onClick={() => handleClick("alarm")}
        style={{ width: '50px', height: '50px'}}
      />
    </div>
  );
};

export default Footer;
