import { useEffect } from "react";
import { useLocation } from "react-router-dom";

export default function ScrollToTop() 
{
    const { pathname } = useLocation();

    useEffect(() => 
    {
      if (document.querySelectorAll(".App")[0])
      {
        document.querySelectorAll(".App")[0].scrollTo(0, 0);
      }
    }, [pathname]);

    return null;
}