import React, { useState } from "react";

const CreateContestContestantForm = () => {
  const [contest, setContest] = useState({
    title: "",
    description: "",
    startDateTime: "",
    endDateTime: "",
    prize: ""
  });

  const [contestant, setContestant] = useState({
    name: "",
    profession: "",
    age: "",
    studies: "",
    notes: ""
  });

  const [isSuperJoker, setIsSuperJoker] = useState(false);

  // Handle input changes
  const handleContestChange = (e) => {
    setContest({ ...contest, [e.target.name]: e.target.value });
  };

  const handleContestantChange = (e) => {
    setContestant({ ...contestant, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      //Create Contest
      const contestRes = await fetch(
        "http://localhost:8000/api/contest-contestants/contests",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            ...contest,
            prize: Number(contest.prize)
          })
        }
      );
      if (!contestRes.ok)
        throw new Error(`Contest creation failed: ${contestRes.status}`);
      const contestData = await contestRes.json();
      const contestId = contestData.id;

      //Create Contestant
      const contestantRes = await fetch(
        "http://localhost:8000/api/contest-contestants/contestants",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            ...contestant,
            age: Number(contestant.age)
          })
        }
      );
      if (!contestantRes.ok)
        throw new Error(
          `Contestant creation failed: ${contestantRes.status}`
        );
      const contestantData = await contestantRes.json();
      const contestantId = contestantData.id;

      //Create Enrollment
      const enrollmentRes = await fetch(
        "http://localhost:8000/api/contest-contestants/contest-contestants",  //TODO change urls
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ contestId, contestantId, isSuperJoker })
        }
      );
      if (!enrollmentRes.ok)
        throw new Error(
          `Enrollment creation failed: ${enrollmentRes.status}`
        );

      alert("Contest, Contestant, and Enrollment created successfully!");

      // Reset form
      setContest({
        title: "",
        description: "",
        startDateTime: "",
        endDateTime: "",
        prize: ""
      });
      setContestant({
        name: "",
        profession: "",
        age: "",
        studies: "",
        notes: ""
      });
      setIsSuperJoker(false);
    } catch (error) {
      console.error(error);
      alert("Something went wrong: " + error.message);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Contest Info</h2>
      <input
        type="text"
        name="title"
        placeholder="Title"
        value={contest.title}
        onChange={handleContestChange}
        required
      />
      <input
        type="text"
        name="description"
        placeholder="Description"
        value={contest.description}
        onChange={handleContestChange}
      />
      <input
        type="datetime-local"
        name="startDateTime"
        value={contest.startDateTime}
        onChange={handleContestChange}
        required
      />
      <input
        type="datetime-local"
        name="endDateTime"
        value={contest.endDateTime}
        onChange={handleContestChange}
        required
      />
      <input
        type="number"
        name="prize"
        placeholder="Prize"
        value={contest.prize}
        onChange={handleContestChange}
      />

      <h2>Contestant Info</h2>
      <input
        type="text"
        name="name"
        placeholder="Name"
        value={contestant.name}
        onChange={handleContestantChange}
        required
      />
      <input
        type="text"
        name="profession"
        placeholder="Profession"
        value={contestant.profession}
        onChange={handleContestantChange}
      />
      <input
        type="number"
        name="age"
        placeholder="Age"
        value={contestant.age}
        onChange={handleContestantChange}
      />
      <select
        name="studies"
        value={contestant.studies}
        onChange={handleContestantChange}
      >
        <option value="">Select studies type</option>
        <option value="PRIMARY">Primary</option>
        <option value="SECONDARY">Secondary</option>
        <option value="BACHELOR">Bachelor</option>
        <option value="MASTER">Master</option>
        <option value="PHD">PhD</option>
        <option value="NONE">None</option>
      </select>
      <input
        type="text"
        name="notes"
        placeholder="Notes"
        value={contestant.notes}
        onChange={handleContestantChange}
      />

      <h2>Enrollment</h2>
      <label>
        Super Joker:
        <input
          type="checkbox"
          checked={isSuperJoker}
          onChange={(e) => setIsSuperJoker(e.target.checked)}
        />
      </label>

      <button type="submit">Create Contest & Contestant</button>
    </form>
  );
};

export default CreateContestContestantForm;
