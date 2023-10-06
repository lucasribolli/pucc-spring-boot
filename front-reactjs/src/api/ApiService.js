import apisauce from "apisauce";

const baseURL = "http://localhost:8080";

export default function create() {
  function apiCallHeaders() {
    return {
      headers: {
        "Cache-Control": "no-cache",
        "Content-Type": "application/json",
        Accept: "application/json",
      },
    };
  }
  function apiCallHeadersWithAuth() {
    return {
      headers: {
        "Cache-Control": "no-cache",
        "Content-Type": "application/json",
        Accept: "application/json",
        "Cookie": localStorage.getItem("token"),
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "POST, GET, PUT",
        "Access-Control-Allow-Headers": "Content-Type"
      },
    };
  }

  const apiCall = apisauce.create({
    baseURL
  });

  function userLogin(data) {
    return apiCall.post("/api/auth/signin", data, apiCallHeaders());
  }

  function getReserves() {
    return apiCall.get("/api/reserves", apiCallHeaders());
  }

  function getUsers() {
    return apiCall.get("/api/users", apiCallHeaders());
  }

  function editReserveStatus(data) {
    return apiCall.put("/api/reserves", data, apiCallHeadersWithAuth());
  }

  function createUser(data) {
    return apiCall.post("/api/auth/signup", data, apiCallHeadersWithAuth());
  }

  return {
    userLogin,
    getReserves,
    editReserveStatus,
    createUser,
    getUsers
  };
}
