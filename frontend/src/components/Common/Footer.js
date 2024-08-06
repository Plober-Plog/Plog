import { useNavigate, useLocation } from "react-router-dom";
import { useEffect } from "react";
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
import "./Footer.css";

const Footer = () => {
  const { activeButton, setActiveButton } = useButtonStore(); 
  const navigate = useNavigate();
  const location = useLocation();

  // 활성 버튼 바뀔 때마다 해당 icon 활성화
  useEffect(() => {
    const path = location.pathname;
    switch (path) {
      case "/sns":
        setActiveButton("sns");
        break;
      case "/profile":
        setActiveButton("profile");
        break;
      case "/":
        setActiveButton("home");
        break;
      case "/chat":
        setActiveButton("chat");
        break;
      case "/alarm":
        setActiveButton("alarm");
        break;
      default:
        setActiveButton("home");
        break;
    }
  }, [location.pathname, setActiveButton]);

  // footer 이동
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
    <div className="footer-container">
      <button className="footer-button" onClick={() => handleClick("sns")}>
        <img
          src={activeButton === "sns" ? snsSelect : snsDefault}
          alt="sns 아이콘"
          className={activeButton === "chat" ? "footer-icon-select" : "footer-icon-select"}
        />
      </button>
      <button className="footer-button" onClick={() => handleClick("profile")}>
        <img
          src={activeButton === "profile" ? profileSelect : profileDefault}
          alt="profile 아이콘"
          className={activeButton === "chat" ? "footer-icon-select" : "footer-icon-select"}
        />
      </button>
      <button className="footer-button" onClick={() => handleClick("home")}>
        <img
          src={activeButton === "home" ? homeSelect : homeDefault}
          alt="home 아이콘"
          className={activeButton === "chat" ? "footer-icon-select" : "footer-icon-select"}
        />
      </button>
      <button className="footer-button" onClick={() => handleClick("chat")}>
        <img
          src={activeButton === "chat" ? chatSelect : chatDefault}
          alt="chat 아이콘"
          className={activeButton === "chat" ? "footer-icon-select" : "footer-icon-select"}
        />
      </button>
      <button className="footer-button" onClick={() => handleClick("alarm")}>
        <img
          src={activeButton === "alarm" ? alarmSelect : alarmDefault}
          alt="alarm 아이콘"
          className={activeButton === "chat" ? "footer-icon-select" : "footer-icon-select"}
        />
      </button>
    </div>
  );
};

export default Footer;
