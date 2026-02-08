import React, { useState } from "react";
import { apiGet, apiPost, ApiError } from "./api";

/* =========================
   Types
========================= */

type LoginRes = {
  accessToken: string;
  tokenType: string;
  expiresInSeconds: number;
};

// Backend may return roomId OR id
type Room = {
  roomId?: number;
  id?: number;
  name: string;
  building: string;
  floor: string;
  capacity: number;
};

 
type BookingRes = {
  bookingId: number;
  status: string;
};

/* =========================
   Helpers
========================= */

// Convert datetime-local → ISO (UTC)
function toIso(dtLocal: string) {
  return new Date(dtLocal).toISOString();
}

// Safe roomId extraction (prevents NaN)
function getRoomId(r: Room): number {
  const id = r.roomId ?? r.id;

  if (typeof id === "number" && Number.isFinite(id)) {
    return id;
  }

  const parsed = Number(id);
  if (Number.isFinite(parsed)) {
    return parsed;
  }

  throw new Error("Invalid room id from backend: " + JSON.stringify(r));
}

/* =========================
   Component
========================= */

export function App() {
  // Auth
  const [token, setToken] = useState("");

  // Register/Login
  const [name, setName] = useState("Srinivas");
  const [email, setEmail] = useState("sri@example.com");
  const [password, setPassword] = useState("P@ssw0rd1");

  // Search
  const [start, setStart] = useState("");
  const [end, setEnd] = useState("");
  const [capacity, setCapacity] = useState(6);

  // Rooms
  const [rooms, setRooms] = useState<Room[]>([]);
  const [selectedRoomId, setSelectedRoomId] = useState<number | null>(null);

  // Booking
  const [title, setTitle] = useState("Standup");

  // UI messages
  const [msg, setMsg] = useState("");
  const [err, setErr] = useState("");

  const authed = !!token;

  /* =========================
     Auth handlers
  ========================= */

  async function register() {
    setErr("");
    setMsg("");
    try {
      await apiPost<void>("/api/v1/auth/register", { 
        name, 
        email, 
        password });
      setMsg("Registered successfully. Please login.");
    } catch (e) {
      setErr((e as ApiError).message || "Register failed");
    }
  }

  async function login() {
    setErr("");
    setMsg("");
    try {
      const r = await apiPost<LoginRes>("/api/v1/auth/login", {
        email,
        password,
      });
      setToken(r.accessToken);
      setMsg("Login successful.");
    } catch (e) {
      setErr((e as ApiError).message || "Login failed");
    }
  }

  async function logout() {
    try {
      if (token) {
        await apiPost<void>("/api/v1/auth/logout", {}, token);
      }
    } finally {
      setToken("");
      setRooms([]);
      setSelectedRoomId(null);
      setMsg("");
      setErr("");
    }
  }

  /* =========================
     Search rooms
  ========================= */

  async function searchRooms() {
    setErr("");
    setMsg("");

    if (!start || !end) {
      setErr("Start and End time are required");
      return;
    }

    try {
      const qs = new URLSearchParams({
        start: toIso(start),
        end: toIso(end),
        capacity: String(capacity),
      }).toString();

      const result = await apiGet<Room[]>(
        `/api/v1/rooms/available?${qs}`,
        token,
      );

      console.log("Rooms API response:", result);

      setRooms(result);
      setSelectedRoomId(null);
      setMsg(`Found ${result.length} room(s).`);
    } catch (e) {
      setErr((e as ApiError).message || "Search failed");
    }
  }

  /* =========================
     Book room
  ========================= */

  async function book(roomId: number) {
    setErr("");
    setMsg("");

    if (!Number.isFinite(roomId)) {
      setErr("Invalid room selected. Please re-search and select again.");
      return;
    }

    try {
      const r = await apiPost<BookingRes>(
        `/api/v1/rooms/${roomId}/bookings`,
        {
          startTime: toIso(start),
          endTime: toIso(end),
          title,
        },
        token,
      );

      setMsg(`✅ Booking successful. BookingId=${r.bookingId}`);
    } catch (e) {
      const ae = e as ApiError;
      if (ae.status === 409) {
        setMsg("⚠️ Conflict: Room already booked for this time.");
      } else {
        setErr(ae.message || "Booking failed");
      }
    }
  }

  /* =========================
     Render
  ========================= */

  return (
    <div
      style={{
        fontFamily: "system-ui",
        maxWidth: 980,
        margin: "0 auto",
        padding: 16,
      }}
    >
      <h2>Meeting Room Booking</h2>

      {!authed ? (
        <div
          style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 16 }}
        >
          <div
            style={{ border: "1px solid #ddd", borderRadius: 12, padding: 12 }}
          >
            <h3>Register</h3>
            <label>
              Name
              <br />
              <input
                value={name}
                onChange={(e) => setName(e.target.value)}
                style={{ width: "100%" }}
              />
            </label>
            <br />
            <label>
              Email
              <br />
              <input
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                style={{ width: "100%" }}
              />
            </label>
            <br />
            <label>
              Password
              <br />
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={{ width: "100%" }}
              />
            </label>
            <br />
            <button type="button" onClick={register}>
              Create Account
            </button>
          </div>

          <div
            style={{ border: "1px solid #ddd", borderRadius: 12, padding: 12 }}
          >
            <h3>Login</h3>
            <label>
              Email
              <br />
              <input
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                style={{ width: "100%" }}
              />
            </label>
            <br />
            <label>
              Password
              <br />
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={{ width: "100%" }}
              />
            </label>
            <br />
            <button type="button" onClick={login}>
              Sign In
            </button>
          </div>
        </div>
      ) : (
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <div>✅ Logged in</div>
          <button type="button" onClick={logout}>
            Logout
          </button>
        </div>
      )}

      <hr style={{ margin: "16px 0" }} />

      <div style={{ border: "1px solid #ddd", borderRadius: 12, padding: 12 }}>
        <h3>Search Available Rooms</h3>

        <div
          style={{
            display: "grid",
            gridTemplateColumns: "1fr 1fr 1fr",
            gap: 12,
          }}
        >
          <label>
            Start
            <br />
            <input
              type="datetime-local"
              value={start}
              onChange={(e) => setStart(e.target.value)}
              style={{ width: "100%" }}
            />
          </label>
          <label>
            End
            <br />
            <input
              type="datetime-local"
              value={end}
              onChange={(e) => setEnd(e.target.value)}
              style={{ width: "100%" }}
            />
          </label>
          <label>
            Min Capacity
            <br />
            <input
              type="number"
              value={capacity}
              onChange={(e) => setCapacity(Number(e.target.value))}
              style={{ width: "100%" }}
            />
          </label>
        </div>

        <div style={{ marginTop: 12 }}>
          <button type="button" disabled={!authed} onClick={searchRooms}>
            Search
          </button>
        </div>

        <div style={{ marginTop: 12 }}>
          <h4>Results</h4>
          {rooms.length === 0 ? (
            <div>No rooms found.</div>
          ) : (
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr>
                  <th align="left">Room</th>
                  <th align="left">Location</th>
                  <th align="left">Capacity</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {rooms.map((r) => {
                  const rid = getRoomId(r);
                  return (
                    <tr key={rid}>
                      <td>{r.name}</td>
                      <td>
                        {r.building}-{r.floor}
                      </td>
                      <td>{r.capacity}</td>
                      <td>
                        <button
                          type="button"
                          onClick={() => setSelectedRoomId(rid)}
                        >
                          Select
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>

        {selectedRoomId !== null && (
          <div
            style={{
              marginTop: 12,
              borderTop: "1px dashed #cccccc",
              paddingTop: 12,
            }}
          >
            <h4>Book Room #{selectedRoomId}</h4>
            <label>
              Title
              <br />
              <input
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                style={{ width: "100%" }}
              />
            </label>
            <br />
            <button type="button" onClick={() => book(selectedRoomId)}>
              Confirm Booking
            </button>
          </div>
        )}
      </div>

      {msg && (
        <div style={{ marginTop: 12, background: "#0eb10e", padding: 10 }}>
          {msg}
        </div>
      )}
      {err && (
        <div
          style={{
            marginTop: 12,
            background: "#ffecec",
            padding: 10,
            color: "#a40000",
          }}
        >
          {err}
        </div>
      )}
    </div>
  );
}
