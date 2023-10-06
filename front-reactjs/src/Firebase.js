import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth"

const firebaseConfig = {
  apiKey: "AIzaSyB8S9pFxFFJM_nhjhNJ_DIpex7DBkxD_oE",
  authDomain: "hospitoque-auth.firebaseapp.com",
  projectId: "hospitoque-auth",
  storageBucket: "hospitoque-auth.appspot.com",
  messagingSenderId: "19084195280",
  appId: "1:19084195280:web:c850c845a7e352222c35d5"
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

const signInWithGoogle = async () => {
    try {
        const result = await signInWithPopup(auth, provider);

        if (!result.user.email) {
            return {
                status: "UNAUTHORIZED"
            };
        }
    
        const name = result.user.displayName;
        const email = result.user.email;
        
        localStorage.setItem("name", name);
        localStorage.setItem("email", email);
        localStorage.setItem("isAuth", true);
        
        return {
            email, 
            status: "AUTHORIZED"
        };
    } catch (error) {
        console.log(error)
    }
}

export {
    signInWithGoogle,
    auth
}